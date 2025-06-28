package com.avegus.telegramconnector.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "users", schema = "bot")
public class User {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    public static User from(Long userId, String username, String state) {
        return new User(
                userId,
                username,
                state,
                LocalDateTime.now()
        );
    }
}
