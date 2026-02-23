package sn.xoslu.tech.ebank.annotations.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sn.xoslu.tech.ebank.annotations.UniqueEmail;
import sn.xoslu.tech.ebank.repositories.CustomerRepository;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private CustomerRepository customerRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) return true;
        return !customerRepository.existsByEmail(email);
    }
}
