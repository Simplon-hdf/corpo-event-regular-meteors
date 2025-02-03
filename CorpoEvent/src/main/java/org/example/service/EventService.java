package org.example.service;

import org.example.model.Event;
import org.example.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEvent(String uuid) {
        return eventRepository.findByIdWithCreatedBy(uuid)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Transactional
    public Event createEvent(Event event) {
        try {
            validateEvent(event);
            Event savedEvent = eventRepository.save(event);
            logger.info("Event created successfully: {}", savedEvent.getUuid());
            return savedEvent;
        } catch (IllegalArgumentException e) {
            logger.error("Validation error while creating event: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error creating event: {}", e.getMessage());
            throw new RuntimeException("Failed to create event", e);
        }
    }

    private void validateEvent(Event event) {
        if (event.getStartDate().isAfter(event.getEndDate())) {
            throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin");
        }
        if (event.getCreatedBy() == null) {
            throw new IllegalArgumentException("L'événement doit avoir un créateur");
        }
        if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre est obligatoire");
        }
        if (event.getDescription() == null || event.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("La description est obligatoire");
        }
    }

    @Transactional
    public Event updateEvent(Event event) {
        try {
            validateEvent(event);
            Event updatedEvent = eventRepository.save(event);
            logger.info("Event updated successfully: {}", updatedEvent.getUuid());
            return updatedEvent;
        } catch (IllegalArgumentException e) {
            logger.error("Validation error while updating event: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating event: {}", e.getMessage());
            throw new RuntimeException("Failed to update event", e);
        }
    }

    @Transactional
    public void deleteEvent(String uuid) {
        try {
            eventRepository.deleteById(uuid);
            logger.info("Event deleted successfully: {}", uuid);
        } catch (Exception e) {
            logger.error("Error deleting event {}: {}", uuid, e.getMessage());
            throw new RuntimeException("Failed to delete event", e);
        }
    }
} 