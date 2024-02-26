package com.sparta.hmpah.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "password_history")
@NoArgsConstructor
public class PasswordHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer sequence;

    public PasswordHistory(User user, String password, Integer sequence) {
        this.user = user;
        this.password = password;
        this.sequence = sequence;
    }
}
