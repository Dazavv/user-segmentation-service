package com.example.segmentation.audit;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entityName;
    private Long entityId;

    private String action;

    private String login;
    private String email;

    @Column(columnDefinition = "TEXT")
    private String details;

    private LocalDateTime timestamp;
}
