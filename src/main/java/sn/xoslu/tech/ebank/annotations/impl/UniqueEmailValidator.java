package sn.xoslu.tech.ebank.annotations.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import sn.xoslu.tech.ebank.annotations.UniqueEmail;
import sn.xoslu.tech.ebank.repositories.CustomerRepository;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    CustomerRepository customerRepository;

    public UniqueEmailValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) return true;
        return !customerRepository.existsByEmail(email);
    }
}
