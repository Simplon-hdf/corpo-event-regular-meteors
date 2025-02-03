package org.example.repository;

import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.events WHERE u.email = :email AND TYPE(u) IN (org.example.model.Admin, org.example.model.Collaborator)")
    User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.events WHERE u.uuid = :uuid AND TYPE(u) IN (org.example.model.Admin, org.example.model.Collaborator)")
    @NonNull
    java.util.Optional<User> findById(@NonNull @Param("uuid") String uuid);

    @Override
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.events WHERE TYPE(u) IN (org.example.model.Admin, org.example.model.Collaborator)")
    @NonNull
    List<User> findAll();
} 