package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("COLLABORATOR")
@Getter @Setter @NoArgsConstructor
public class Collaborator extends User {
    public Collaborator(String uuid, String firstName, String lastName, String email, String password) {
        super(uuid, firstName, lastName, email, password);
    }
} 