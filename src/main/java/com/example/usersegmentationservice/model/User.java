package com.example.usersegmentationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    private Long id;

    private String email;

    private String name;
    private String surname;


    @ManyToMany
    @JoinTable(
            name = "user_segments",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "segment_id")
    )
    private Set<Segment> segments = new HashSet<>();

}
