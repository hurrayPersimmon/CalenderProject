package com.example.calenderproject.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class MemberEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String username;
    private String password;

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
}
