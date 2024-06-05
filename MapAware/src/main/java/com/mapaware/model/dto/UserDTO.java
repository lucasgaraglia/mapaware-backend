    package com.mapaware.model.dto;


    import com.mapaware.model.entity.RoleEnum;
    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDate;
    import java.util.*;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class UserDTO {

        private String username;
        private String email;
        private String name;
        private String lastname;
        private LocalDate birthdate;

        @Enumerated(EnumType.STRING)
        private RoleEnum role;


        private String profileImage;

        private Collection<EventDTO> events;


    }
