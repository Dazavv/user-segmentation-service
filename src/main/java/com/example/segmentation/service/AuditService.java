package com.example.segmentation.service;

import com.example.authorization.jwt.JwtAuthentication;
import com.example.authorization.model.AuthUser;
import com.example.segmentation.audit.AuditLog;
import com.example.segmentation.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public void logChange(String entityName, Long entityId, String action, String details) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = null;
        String email = null;

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof JwtAuthentication jwtAuth) {
            login = jwtAuth.getLogin();
            email = jwtAuth.getEmail();
        }

        AuditLog log = new AuditLog();
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setLogin(login);
        log.setEmail(email);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }
}
