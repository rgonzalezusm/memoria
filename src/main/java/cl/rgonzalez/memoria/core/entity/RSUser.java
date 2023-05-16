package cl.rgonzalez.memoria.core.entity;

import cl.rgonzalez.memoria.core.RSRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

import lombok.Data;

@Entity
@Table(name = "rs_user")
@Data
public class RSUser extends RSAbstractEntity {

    @Column(name = "username", unique = true, length = 128)
    private String username = "";
    private String name = "";
    @JsonIgnore
    private String hashedPassword = "";
    //
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tp_roles")
    private Set<RSRole> roles;

}
