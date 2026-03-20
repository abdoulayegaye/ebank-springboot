package sn.xoslu.tech.ebank.services.impl;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.xoslu.tech.ebank.entities.Account;
import sn.xoslu.tech.ebank.entities.Operation;
import sn.xoslu.tech.ebank.enums.TypeOperation;
import sn.xoslu.tech.ebank.exceptions.NotFoundException;
import sn.xoslu.tech.ebank.repositories.AccountRepository;
import sn.xoslu.tech.ebank.repositories.OperationRepository;
import sn.xoslu.tech.ebank.services.BankStatementService;

import java.io.ByteArrayOutputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankStatementServiceImpl implements BankStatementService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(0, 71, 171);
    private static final DeviceRgb HEADER_BG = new DeviceRgb(230, 239, 255);
    private static final DeviceRgb ROW_DEPOSIT = new DeviceRgb(232, 255, 232);
    private static final DeviceRgb ROW_WITHDRAWAL = new DeviceRgb(255, 235, 235);

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    @Override
    public byte[] generateBankStatement(String accountNumber, LocalDate startDate, LocalDate endDate) {
        Account account = accountRepository.findByNumero(accountNumber)
                .orElseThrow(() -> new NotFoundException("Compte introuvable : " + accountNumber));

        Instant start = startDate != null
                ? startDate.atStartOfDay(ZoneOffset.UTC).toInstant()
                : Instant.EPOCH;
        Instant end = endDate != null
                ? endDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant()
                : Instant.now();

        List<Operation> operations = operationRepository.findByAccountAndDateRange(accountNumber, start, end);

        double totalDeposits = operations.stream()
                .filter(o -> o.getType() == TypeOperation.DEPOSIT)
                .mapToDouble(Operation::getAmount)
                .sum();
        double totalWithdrawals = operations.stream()
                .filter(o -> o.getType() == TypeOperation.WITHDRAWAL)
                .mapToDouble(Operation::getAmount)
                .sum();
        double closingBalance = account.getBalance();
        double openingBalance = closingBalance - totalDeposits + totalWithdrawals;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(36, 36, 36, 36);

        addHeader(document, account, startDate, endDate);
        addAccountInfo(document, account);
        addPeriodInfo(document, startDate, endDate);
        addOperationsTable(document, operations);
        addSummary(document, openingBalance, totalDeposits, totalWithdrawals, closingBalance, account.getCurrency());
        addFooter(document);

        document.close();
        return baos.toByteArray();
    }

    private void addHeader(Document document, Account account, LocalDate startDate, LocalDate endDate) {
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .setWidth(UnitValue.createPercentValue(100));

        Cell bankCell = new Cell()
                .add(new Paragraph("eBank").setFontSize(22).setBold().setFontColor(PRIMARY_COLOR))
                .add(new Paragraph("Banque Électronique").setFontSize(10).setFontColor(ColorConstants.GRAY))
                .setBorder(null)
                .setTextAlignment(TextAlignment.LEFT);

        Cell titleCell = new Cell()
                .add(new Paragraph("RELEVÉ DE COMPTE").setFontSize(16).setBold().setFontColor(PRIMARY_COLOR))
                .setBorder(null)
                .setTextAlignment(TextAlignment.RIGHT);

        headerTable.addCell(bankCell);
        headerTable.addCell(titleCell);
        document.add(headerTable);
        document.add(new Paragraph("\n").setFontSize(4));
    }

    private void addAccountInfo(Document document, Account account) {
        Paragraph sectionTitle = new Paragraph("INFORMATIONS DU COMPTE")
                .setFontSize(11)
                .setBold()
                .setFontColor(PRIMARY_COLOR)
                .setBackgroundColor(HEADER_BG)
                .setPadding(6);
        document.add(sectionTitle);

        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 2, 1, 2}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(10);

        addInfoRow(infoTable, "N° Compte", account.getNumero(), "Devise", account.getCurrency());
        addInfoRow(infoTable, "Titulaire",
                account.getCustomer() != null ? account.getCustomer().getName() : "-",
                "Statut", account.isActive() ? "Actif" : "Inactif");

        document.add(infoTable);
    }

    private void addInfoRow(Table table, String label1, String value1, String label2, String value2) {
        table.addCell(new Cell().add(new Paragraph(label1).setBold().setFontSize(9)).setBackgroundColor(HEADER_BG).setBorderColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph(value1).setFontSize(9)).setBorderColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph(label2).setBold().setFontSize(9)).setBackgroundColor(HEADER_BG).setBorderColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph(value2).setFontSize(9)).setBorderColor(ColorConstants.LIGHT_GRAY));
    }

    private void addPeriodInfo(Document document, LocalDate startDate, LocalDate endDate) {
        String periodText = "Période : "
                + (startDate != null ? startDate.format(DATE_FORMATTER) : "Depuis le début")
                + "  →  "
                + (endDate != null ? endDate.format(DATE_FORMATTER) : "Aujourd'hui");

        document.add(new Paragraph(periodText)
                .setFontSize(10)
                .setItalic()
                .setFontColor(ColorConstants.DARK_GRAY)
                .setMarginBottom(10));
    }

    private void addOperationsTable(Document document, List<Operation> operations) {
        Paragraph sectionTitle = new Paragraph("OPÉRATIONS")
                .setFontSize(11)
                .setBold()
                .setFontColor(PRIMARY_COLOR)
                .setBackgroundColor(HEADER_BG)
                .setPadding(6);
        document.add(sectionTitle);

        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 3.5f, 1.5f, 1.5f, 2}))
                .setWidth(UnitValue.createPercentValue(100));

        String[] headers = {"Date", "N° Opération", "Type", "Montant", "Solde courant"};
        for (String h : headers) {
            table.addHeaderCell(new Cell()
                    .add(new Paragraph(h).setBold().setFontSize(9))
                    .setBackgroundColor(PRIMARY_COLOR)
                    .setFontColor(ColorConstants.WHITE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorderColor(PRIMARY_COLOR));
        }

        if (operations.isEmpty()) {
            Cell noDataCell = new Cell(1, 5)
                    .add(new Paragraph("Aucune opération sur la période sélectionnée.")
                            .setItalic().setFontSize(9).setTextAlignment(TextAlignment.CENTER))
                    .setBorderColor(ColorConstants.LIGHT_GRAY);
            table.addCell(noDataCell);
        } else {
            double runningBalance = 0;
            for (Operation op : operations) {
                boolean isDeposit = op.getType() == TypeOperation.DEPOSIT;
                runningBalance += isDeposit ? op.getAmount() : -op.getAmount();
                DeviceRgb rowColor = isDeposit ? ROW_DEPOSIT : ROW_WITHDRAWAL;

                String dateStr = op.getCreatedAt() != null
                        ? LocalDateTime.ofInstant(op.getCreatedAt(), ZoneOffset.UTC).format(DATE_FORMATTER)
                        : "-";
                String numeroShort = op.getNumero() != null && op.getNumero().length() > 18
                        ? op.getNumero().substring(0, 18) + "..."
                        : op.getNumero();
                String typeLabel = isDeposit ? "DÉPÔT" : "RETRAIT";
                String amountStr = (isDeposit ? "+" : "-") + String.format("%,.2f", op.getAmount());
                String balanceStr = String.format("%,.2f", runningBalance);

                addTableRow(table, rowColor, dateStr, numeroShort, typeLabel, amountStr, balanceStr, isDeposit);
            }
        }

        document.add(table);
        document.add(new Paragraph("\n").setFontSize(4));
    }

    private void addTableRow(Table table, DeviceRgb bg, String date, String numero, String type,
                              String amount, String balance, boolean isDeposit) {
        DeviceRgb amountColor = isDeposit ? new DeviceRgb(0, 128, 0) : new DeviceRgb(200, 0, 0);

        table.addCell(new Cell().add(new Paragraph(date).setFontSize(8)).setBackgroundColor(bg).setBorderColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph(numero).setFontSize(7)).setBackgroundColor(bg).setBorderColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph(type).setFontSize(8).setBold().setFontColor(amountColor)).setBackgroundColor(bg).setBorderColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add(new Paragraph(amount).setFontSize(8).setBold().setFontColor(amountColor)).setBackgroundColor(bg).setBorderColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(new Paragraph(balance).setFontSize(8)).setBackgroundColor(bg).setBorderColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.RIGHT));
    }

    private void addSummary(Document document, double openingBalance, double totalDeposits,
                             double totalWithdrawals, double closingBalance, String currency) {
        Paragraph sectionTitle = new Paragraph("RÉSUMÉ")
                .setFontSize(11)
                .setBold()
                .setFontColor(PRIMARY_COLOR)
                .setBackgroundColor(HEADER_BG)
                .setPadding(6);
        document.add(sectionTitle);

        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(10);

        String[] labels = {"Solde d'ouverture", "Total dépôts", "Total retraits", "Solde de clôture"};
        String[] values = {
                String.format("%,.2f %s", openingBalance, currency),
                String.format("+%,.2f %s", totalDeposits, currency),
                String.format("-%,.2f %s", totalWithdrawals, currency),
                String.format("%,.2f %s", closingBalance, currency)
        };
        DeviceRgb[] colors = {
                new DeviceRgb(100, 100, 200),
                new DeviceRgb(0, 128, 0),
                new DeviceRgb(200, 0, 0),
                PRIMARY_COLOR
        };

        for (int i = 0; i < labels.length; i++) {
            summaryTable.addCell(new Cell()
                    .add(new Paragraph(labels[i]).setFontSize(8).setBold().setFontColor(ColorConstants.WHITE))
                    .add(new Paragraph(values[i]).setFontSize(10).setBold().setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(colors[i])
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(8)
                    .setBorder(null));
        }

        document.add(summaryTable);
    }

    private void addFooter(Document document) {
        String generatedAt = LocalDateTime.now(ZoneOffset.UTC).format(DATETIME_FORMATTER);
        document.add(new Paragraph("Document généré le " + generatedAt + " (UTC)")
                .setFontSize(8)
                .setItalic()
                .setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER));
    }
}
