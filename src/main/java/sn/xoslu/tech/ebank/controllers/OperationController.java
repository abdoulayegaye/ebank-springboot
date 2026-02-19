package sn.xoslu.tech.ebank.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.xoslu.tech.ebank.dtos.ApiResponse;
import sn.xoslu.tech.ebank.dtos.OperationDTO;
import sn.xoslu.tech.ebank.services.OperationService;

import java.util.List;

@RestController
@RequestMapping("/operations")
@RequiredArgsConstructor
@Tag(name = "Gestion des opérations", description = "Donne toutes les opérations qui sont liées á la gestion des opérations")
public class OperationController {

    private final OperationService operationService;

    @PostMapping("/deposit")
    @Operation(summary = "Dépot", description = "Dépot")
    public ResponseEntity<ApiResponse<Void>> deposit(
            @RequestParam String accountNumber,
            @RequestParam double amount) {

        operationService.deposit(accountNumber, amount);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Dépôt effectué avec succès.",
                        null
                )
        );
    }

    @PostMapping("/withdraw")
    @Operation(summary = "Retrait", description = "Retrait")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @RequestParam String accountNumber,
            @RequestParam double amount) {

        operationService.withdraw(accountNumber, amount);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Retrait effectué avec succès.",
                        null
                )
        );
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfert", description = "Transfert")
    public ResponseEntity<ApiResponse<Void>> transfer(
            @RequestParam String fromAccount,
            @RequestParam String toAccount,
            @RequestParam double amount) {

        operationService.transfer(fromAccount, toAccount, amount);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Virement effectué avec succès.",
                        null
                )
        );
    }

    @GetMapping("/{accountNumber}")
    @Operation(summary = "Historique des opérations via numero de compte", description = "Lister les opérations via numero de compte")
    public ResponseEntity<ApiResponse<List<OperationDTO>>> getAccountOperations(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Historique des opérations récupéré avec succès.",
                        operationService.getAccountOperations(accountNumber)
                )
        );
    }
}

