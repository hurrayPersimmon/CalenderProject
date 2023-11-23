package com.example.calenderproject.Dto;

import com.example.calenderproject.Entity.EventEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class EventDTO {
    private Long id;
    private String title;
    private String username;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    private boolean allDay;

    public EventEntity toEntity() {
        return new EventEntity(id, title, username, startTime, endTime, allDay);
    }


}
