package com.example.usersegmentationservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "segments")
public class Segment {

    @Id
    private Long id;

    private String code; //unique
    private String info;

    @ManyToMany(mappedBy = "segments")
    private Set<User> users = new HashSet<>();
}
