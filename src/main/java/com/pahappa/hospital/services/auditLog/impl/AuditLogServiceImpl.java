package com.pahappa.hospital.services.auditLog.impl;

import com.pahappa.hospital.daos.AuditLogDao;
import com.pahappa.hospital.models.AuditLog;
import com.pahappa.hospital.services.auditLog.AuditLogService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class AuditLogServiceImpl implements AuditLogService {

    @Inject
    private AuditLogDao auditLogDao;

    // Constructor injection
    public AuditLogServiceImpl() {
    }

    @Override
    public void logAction(String action, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setDetails(details);
        auditLog.setDate(LocalDate.now());

        auditLogDao.saveAuditLog(auditLog);
    }

    @Override
    public List<AuditLog> getAllAuditLogs() {
        return auditLogDao.getAllAuditLogs();
    }

    @Override
    public List<AuditLog> getAuditLogsByDate(LocalDate date) {
        if (date == null) {
            return getAllAuditLogs();
        }
        return auditLogDao.getAuditLogsByDate(date);
    }

    @Override
    public List<AuditLog> getAuditLogsByAction(String action) {
        if (action == null || action.trim().isEmpty()) {
            return getAllAuditLogs();
        }
        return auditLogDao.getAuditLogsByAction(action);
    }

    @Override
    public List<AuditLog> getAuditLogsByDateAndAction(LocalDate date, String action) {
        // First get logs by date if date is specified
        List<AuditLog> logs = (date != null) ? getAuditLogsByDate(date) : getAllAuditLogs();

        // Then filter by action if specified
        if (action != null && !action.trim().isEmpty()) {
            logs.removeIf(log -> !log.getAction().equalsIgnoreCase(action));
        }

        return logs;
    }
}