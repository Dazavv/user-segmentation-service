package com.example.segmentation.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 30
    )
    private String email;

    @Column(length = 20)
    private String name;

    @Column(length = 30)
    private String surname;

    @ManyToMany(mappedBy = "users")
    private Set<Segment> segments = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", segments=" + segments +
                '}';
    }
}
