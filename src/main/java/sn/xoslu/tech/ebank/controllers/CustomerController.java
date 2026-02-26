package sn.xoslu.tech.ebank.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.xoslu.tech.ebank.dtos.ApiResponse;
import sn.xoslu.tech.ebank.dtos.CustomerDTO;
import sn.xoslu.tech.ebank.services.CustomerService;
import sn.xoslu.tech.ebank.utils.PageResponse;

import java.util.List;

import static sn.xoslu.tech.ebank.abilities.CustomerAbility.*;

@RestController
@RequestMapping(_PATH)
@RequiredArgsConstructor
@Tag(name = _ABILITY_TITLE, description = _ABILITY_DESCRIPTION)
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(description = _ABILITY_CREATE, summary = _ABILITY_CREATE)
    public ResponseEntity<ApiResponse<CustomerDTO>> create(@RequestBody @Valid CustomerDTO customer) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "Client créé avec succès.",
                        customerService.createCustomer(customer)
                ));
    }

    @GetMapping("/{id}")
    @Operation(description = _ABILITY_GET_BY_ID, summary = _ABILITY_GET_BY_ID)
    public ResponseEntity<ApiResponse<CustomerDTO>> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Client trouvé avec succès.",
                        customerService.getCustomerById(id)
                ));
    }

    @GetMapping
    @Operation(description = _ABILITY_GET_ALL, summary = _ABILITY_GET_ALL)
    //@PreAuthorize("hasRole('USER')")
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
    @Operation(description = _ABILITY_UPDATE, summary = _ABILITY_UPDATE)
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
    @Operation(description = _ABILITY_DELETE, summary = _ABILITY_DELETE)
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

    @GetMapping("/check-email")
    @Operation(description = _ABILITY_EMAIL_EXISTS, summary = _ABILITY_EMAIL_EXISTS)
    public ResponseEntity<ApiResponse<?>> checkEmail(@RequestParam String email) {
        boolean exists = customerService.emailExists(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Vérification Email effectuée avec succès.",
                        exists
                ));
    }

    @GetMapping("/search")
    @Operation(description = _ABILITY_SEARCH_WITH_PAGINATION, summary = _ABILITY_SEARCH_WITH_PAGINATION)
    public ResponseEntity<ApiResponse<PageResponse<CustomerDTO>>> getAllCustomers(
            @RequestParam(value = "q", defaultValue = "")    String query,
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "10")  int size,
            @RequestParam(defaultValue = "id")  String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PageResponse<CustomerDTO> results =
                customerService.searchWithPagination(query, page, size, sortBy, sortDir);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        (results.getTotalPages() > 0) ? "Liste des clients trouvés avec succès." : "Aucun client trouvé.",
                        results
                )
        );
    }
}
