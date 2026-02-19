package sn.xoslu.tech.ebank.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.xoslu.tech.ebank.dtos.ApiResponse;
import sn.xoslu.tech.ebank.dtos.CustomerDTO;
import sn.xoslu.tech.ebank.services.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Gestion des clients", description = "Donne toutes les opérations qui sont liées á la gestion des clients")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    @Operation(description = "Créer client", summary = "Créer client")
    public ResponseEntity<ApiResponse<CustomerDTO>> create(@RequestBody CustomerDTO customer) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "Client créé avec succès.",
                        customerService.createCustomer(customer)
                ));
    }

    @GetMapping("/{id}")
    @Operation(description = "Récupérer client via ID", summary = "Récupérer client")
    public ResponseEntity<ApiResponse<CustomerDTO>> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Client trouvé avec succès.",
                        customerService.getCustomerById(id)
                ));
    }

    @GetMapping("/pagined")
    @Operation(description = "Liste paginée de tous les clients", summary = "Lister tous les clients")
    public ResponseEntity<ApiResponse<Page<CustomerDTO>>> getAllPagined(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Liste des clients trouvés avec succès.",
                        customerService.getAllCustomersPagined(page, size, sortBy, sortDir)
                ));
    }

    @GetMapping
    @Operation(description = "Lister tous les clients", summary = "Lister tous les clients")
    @PreAuthorize("hasRole('USER')")
    //@PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ApiResponse<List<CustomerDTO>>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Liste des clients trouvés avec succès.",
                        customerService.getAllCustomers()
                ));
    }

    @PutMapping("/{id}")
    @Operation(description = "Modifier client", summary = "Modifier client")
    public ResponseEntity<ApiResponse<CustomerDTO>> update(
            @PathVariable Long id,
            @RequestBody CustomerDTO customer) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Client modifié avec succès.",
                        customerService.updateCustomer(id, customer)
                ));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Supprimer client", summary = "Supprimer client")
    public ResponseEntity<ApiResponse<CustomerDTO>> delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Client modifié avec succès.",
                        null
                ));
    }
}
