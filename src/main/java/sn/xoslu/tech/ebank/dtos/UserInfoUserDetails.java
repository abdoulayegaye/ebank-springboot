package sn.xoslu.tech.ebank.dtos;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sn.xoslu.tech.ebank.entities.Role;
import sn.xoslu.tech.ebank.entities.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserInfoUserDetails implements UserDetails {

    private String username;
    private String lastname;
    private String firstname;
    private String email;
    private String phone;
    private Role role;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoUserDetails(User user) {
        username = user.getUsername();
        lastname = user.getLastname();
        firstname = user.getFirstname();
        email = user.getEmail();
        phone = user.getPhone();
        role = user.getRole();
        password = user.getPassword();
        authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}