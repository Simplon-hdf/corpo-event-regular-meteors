package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
@Getter @Setter @NoArgsConstructor
public class Admin extends User {
    public Admin(String uuid, String firstName, String lastName, String email, String password) {
        super(uuid, firstName, lastName, email, password);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "uuid='" + getUuid() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
} 