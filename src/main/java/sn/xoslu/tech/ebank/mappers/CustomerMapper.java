package sn.xoslu.tech.ebank.mappers;

import org.springframework.data.domain.Page;
import sn.xoslu.tech.ebank.dtos.CustomerDTO;
import sn.xoslu.tech.ebank.entities.Customer;

import java.util.List;

public interface CustomerMapper {
    CustomerDTO toDTO(Customer customer);
    Customer toEntity(CustomerDTO dto);
    List<CustomerDTO> toDTOList(List<Customer> customers);
    Page<CustomerDTO> toDTOPage(Page<Customer> customers);
}
