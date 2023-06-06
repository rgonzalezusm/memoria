package cl.rgonzalez.memoria.core.entity;

import cl.rgonzalez.memoria.core.RSRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "rs_user")
@Data
@EqualsAndHashCode(callSuper = true)
public class RSEntityUser extends RSAbstractEntity {

    @Column(name = "username", unique = true, length = 128)
    private String username = "";
    private String name = "";
    @JsonIgnore
    private String hashedPassword = "";
    //
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rs_roles")
    private Set<RSRole> roles;

}
