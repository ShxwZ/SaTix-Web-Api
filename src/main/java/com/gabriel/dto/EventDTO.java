package com.gabriel.dto;

import java.time.LocalDateTime;

/*
 *   id_event
 *   address
 *   description
 *   finish_date
 *   max_assistants
 *   min_age
 *   name
 *   start_date
 *   organizer
 */
public record EventDTO(Long id,
                       String address,
                       String description,
                       LocalDateTime finish_date,
                       int max_assistants,
                       int assistants,
                       int min_age,
                       String name,
                       LocalDateTime start_date,
                       Long organizer) {
}
