package com.mapaware.controller;

import com.mapaware.model.dto.EventDTO;
import com.mapaware.model.entity.EventEntity;
import com.mapaware.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'PRO')")
    public ResponseEntity<EventEntity> postEvent(@RequestBody EventEntity event){

        eventService.postEvent(event);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'PRO')")
    public ResponseEntity<Collection<EventEntity>> getEvents(){
        Collection<EventEntity> events = eventService.getEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/filtered")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'PRO')")
    public ResponseEntity<Collection<EventEntity>> getEventsFiltered(@RequestParam(name = "cat") String category){
        Collection<EventEntity> events = eventService.getEventsFiltered(category);
        return ResponseEntity.ok(events);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'PRO')")
    public ResponseEntity<Object> deleteEvent(@PathVariable Long id){
        eventService.deleteEventById(id);
        return ResponseEntity.ok("Event deleted.");
    }

    @GetMapping("/all-pag")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Collection<EventDTO>> getEventsPagination(@RequestParam(name = "pag") int pag,
                                                                    @RequestParam(name = "cant") int cant) {
        Collection<EventDTO> events = eventService.getEventsPagination(pag, cant);
        return ResponseEntity.ok(events);
    }
}
