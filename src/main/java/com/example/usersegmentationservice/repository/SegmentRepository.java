package com.example.usersegmentationservice.repository;

import com.example.usersegmentationservice.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SegmentRepository extends JpaRepository<Segment, Long> {
    Optional<Segment> findByCode(String code);
}
