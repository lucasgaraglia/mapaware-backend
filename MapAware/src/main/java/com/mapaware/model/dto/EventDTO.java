package com.mapaware.model.dto;


import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDTO {

    private Long id;
    private LocalDateTime dateTime;
    private String description;
    private String category;
    private int degree;
    private String latitude;
    private String longitude;

}
