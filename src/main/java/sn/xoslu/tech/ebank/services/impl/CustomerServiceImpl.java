package sn.xoslu.tech.ebank.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sn.xoslu.tech.ebank.dtos.CustomerDTO;
import sn.xoslu.tech.ebank.entities.Customer;
import sn.xoslu.tech.ebank.exceptions.BadRequestException;
import sn.xoslu.tech.ebank.exceptions.NotFoundException;
import sn.xoslu.tech.ebank.mappers.CustomerMapper;
import sn.xoslu.tech.ebank.repositories.CustomerRepository;
import sn.xoslu.tech.ebank.services.CustomerService;
import sn.xoslu.tech.ebank.utils.Tools;

import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new BadRequestException("Un client avec cet email existe déjà");
        }
        Customer c = customerMapper.toEntity(customer);
        c.setState(true);
        Tools.validateEmail(customer.getEmail());
        return customerMapper.toDTO(customerRepository.save(c));
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Client introuvable avec id : " + id)
        );
        return customerMapper.toDTO(customer);
    }

    @Override
    public Page<CustomerDTO> getAllCustomersPagined(int page, int size, String sortBy, String sortOrder) {
        int _page = page <= 1 ? 0 : page;
        int _size = size <= 0 ? 10 : Math.min(size, 100);
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(_page, _size, sort);
        return customerMapper.toDTOPage(customerRepository.getCustomers(pageable));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerMapper.toDTOList(customerRepository.findAll());
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customer) {
        CustomerDTO existing = getCustomerById(id);

        existing.setName(customer.getName());
        existing.setEmail(customer.getEmail());

        return customerMapper.toDTO(customerRepository.save(customerMapper.toEntity(customer)));
    }

    @Override
    public void deleteCustomer(Long id) {
        CustomerDTO customer = getCustomerById(id);
        Customer c = customerMapper.toEntity(customer);
        c.setState(false);
        customerRepository.save(c);
    }

    @Override
    public void changeState(Long id, boolean state) {
        CustomerDTO customer = getCustomerById(id);
        Customer c = customerMapper.toEntity(customer);
        c.setState(state);
        customerRepository.save(c);
    }
}
