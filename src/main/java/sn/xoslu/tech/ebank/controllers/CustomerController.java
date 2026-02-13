package sn.xoslu.tech.ebank.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.xoslu.tech.ebank.dtos.ApiResponse;
import sn.xoslu.tech.ebank.dtos.CustomerDTO;
import sn.xoslu.tech.ebank.entities.Customer;
import sn.xoslu.tech.ebank.services.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
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
