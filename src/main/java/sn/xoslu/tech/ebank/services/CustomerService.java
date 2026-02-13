package sn.xoslu.tech.ebank.services;

import org.springframework.data.domain.Page;
import sn.xoslu.tech.ebank.dtos.CustomerDTO;

import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customer);
    CustomerDTO getCustomerById(Long id);
    Page<CustomerDTO> getAllCustomersPagined(int page, int size, String sortBy, String sortOrder);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO updateCustomer(Long id, CustomerDTO customer);
    void deleteCustomer(Long id);
    void changeState(Long id, boolean state);
}
