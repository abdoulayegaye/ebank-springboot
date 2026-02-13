package sn.xoslu.tech.ebank.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @Column(name = "user_created")
    private String userCreated;

    @Column(name = "user_updated")
    private String userUpdated;

    /*@PreUpdate
    public void preUpdate() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if(username != null){
                userUpdated = username;
            }
        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    @PrePersist
    public void preSave() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if(username != null){
                userCreated = username;
            }
        } catch (Exception e){
            System.out.println("ERROR");
        }
    }*/
}
