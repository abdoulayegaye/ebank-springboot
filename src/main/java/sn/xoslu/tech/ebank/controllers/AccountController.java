package sn.xoslu.tech.ebank.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.xoslu.tech.ebank.dtos.AccountDTO;
import sn.xoslu.tech.ebank.dtos.AccountResponseDTO;
import sn.xoslu.tech.ebank.dtos.ApiResponse;
import sn.xoslu.tech.ebank.services.AccountService;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Gestion des comptes", description = "Donne toutes les opérations qui sont liées á la gestion des comptes")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Créer compte", description = "Créer compte")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> create(@RequestBody AccountDTO account) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "Compte créé avec succès.",
                        accountService.createAccount(account)
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer compte", description = "Pécupérer compte via ID")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Compte trouvé avec succès.",
                        accountService.getAccountById(id)
                ));
    }

    @GetMapping
    @Operation(summary = "lister tous les comptes", description = "Lister tous les comptes")
    public ResponseEntity<ApiResponse<List<AccountResponseDTO>>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Liste des comptes récupérée avec succès.",
                        accountService.getAllAccounts()
                ));
    }

    @GetMapping("/{accountNumber}/balance")
    @Operation(summary = "Récupérer le solde compte", description = "Récupérer le solde compte")
    public ResponseEntity<ApiResponse<Double>> getBalance(
            @PathVariable String accountNumber) {

        return ResponseEntity
                .ok(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Solde récupéré avec succès.",
                        accountService.getBalance(accountNumber)
                ));
    }

    @PutMapping("/{id}/close")
    @Operation(summary = "Fermer compte", description = "Fermer compte")
    public ResponseEntity<ApiResponse<Void>> close(@PathVariable Long id) {
        accountService.closeAccount(id);

        return ResponseEntity
                .ok(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Compte clôturé avec succès.",
                        null
                ));
    }

    @PutMapping("/{id}/open")
    @Operation(summary = "Ouvrir compte", description = "Ouvrir compte")
    public ResponseEntity<ApiResponse<Void>> open(@PathVariable Long id) {
        accountService.openAccount(id);

        return ResponseEntity
                .ok(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Compte clôturé avec succès.",
                        null
                ));
    }
}

