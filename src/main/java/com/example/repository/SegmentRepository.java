package com.example.repository;

import com.example.model.entity.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SegmentRepository extends JpaRepository<Segment, Long> {
    Optional<Segment> findByCode(String code);
}
