package com.example.calenderproject.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    @JoinColumn(name="member_id")
    private MemberEntity memberEntity;

    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endTime;
}
