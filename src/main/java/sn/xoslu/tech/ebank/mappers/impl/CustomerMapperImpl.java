package sn.xoslu.tech.ebank.mappers.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sn.xoslu.tech.ebank.dtos.CustomerDTO;
import sn.xoslu.tech.ebank.entities.Customer;
import sn.xoslu.tech.ebank.mappers.CustomerMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerMapperImpl implements CustomerMapper {
    @Override
    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;

        CustomerDTO dto = new CustomerDTO();

        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());

        return dto;
    }

    @Override
    public Customer toEntity(CustomerDTO dto) {
        if (dto == null) return null;

        return Customer.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    @Override
    public List<CustomerDTO> toDTOList(List<Customer> customers) {
        return customers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CustomerDTO> toDTOPage(Page<Customer> customers) {
        if (customers == null) {
            return Page.empty();
        }

        return customers.map(this::toDTO);
    }
}
