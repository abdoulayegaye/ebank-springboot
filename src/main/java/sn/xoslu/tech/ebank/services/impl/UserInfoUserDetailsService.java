package sn.xoslu.tech.ebank.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import sn.xoslu.tech.ebank.dtos.UserInfoUserDetails;
import sn.xoslu.tech.ebank.entities.User;
import sn.xoslu.tech.ebank.repositories.UserRepository;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userInfo = repository.findByUsername(username);
        return userInfo.map(UserInfoUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
    }
}