package com.gabriel.repositories;

import com.gabriel.models.Event;
import com.gabriel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.organizer = ?1")
    List<Optional<Event>> findAllEventByAdmin(User user);
}
