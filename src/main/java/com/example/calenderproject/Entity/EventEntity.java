package com.example.calenderproject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class EventEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @JsonIgnore
    @ManyToOne(targetEntity = MemberEntity.class, fetch = FetchType.EAGER , cascade = CascadeType.REMOVE)
    @JoinColumn(name="member_id")
    private MemberEntity memberEntity;

    private boolean allDay;


    public EventEntity(Long id, String title, String username, LocalDateTime startTime, LocalDateTime endTime, boolean allDay){
        this.id = id;
        this.title = title;
        this.username = username;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allDay = allDay;
    }
}
