package sn.xoslu.tech.ebank.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "phone")
})
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String username;
    @Column(name = "is_admin", columnDefinition = "bit not null default 0")
    private boolean isAdmin = false;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column(name = "is_enabled", columnDefinition = "bit not null default 0")
    private boolean isEnabled = true;
    @Column(name = "account_is_enabled", columnDefinition = "bit not null default 1")
    private boolean accountIsEnabled = true ;
    @Column(name = "account_is_not_expired", columnDefinition = "bit not null default 1")
    private boolean accountIsNotExpired = true;
    @Column(name = "account_is_not_locked", columnDefinition = "bit not null default 1")
    private boolean accountIsNotLocked = true;
    @Column(name = "credential_not_expired", columnDefinition = "bit not null default 1")
    private boolean credentialNotExpired = true;
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;
}