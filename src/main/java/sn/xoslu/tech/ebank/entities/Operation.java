package sn.xoslu.tech.ebank.entities;

import jakarta.persistence.*;
import lombok.*;
import sn.xoslu.tech.ebank.enums.TypeOperation;

@Entity
@Table(name = "operations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Operation extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero;
    @Enumerated(EnumType.STRING)
    private TypeOperation type;
    private double amount;
    @ManyToOne
    private Account account;
}
