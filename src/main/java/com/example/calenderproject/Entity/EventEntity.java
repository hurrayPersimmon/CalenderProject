package com.example.calenderproject.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class EventEntity {

    @Id
    private Long id;

    private String title;
    private Date start;
    private Date end;
}
