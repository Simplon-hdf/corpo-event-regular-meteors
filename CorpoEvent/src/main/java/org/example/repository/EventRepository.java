package org.example.repository;

import org.example.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.createdBy WHERE e.uuid = :uuid")
    Optional<Event> findByIdWithCreatedBy(@Param("uuid") String uuid);
} 