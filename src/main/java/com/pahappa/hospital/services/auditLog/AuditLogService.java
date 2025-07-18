package com.pahappa.hospital.services.auditLog;

import com.pahappa.hospital.models.AuditLog;
import java.time.LocalDate;
import java.util.List;

public interface AuditLogService {

    /**
     * Logs an action to the audit log
     * @param action The action being performed (e.g., "USER_LOGIN", "PATIENT_UPDATE")
     * @param details Additional details about the action
     */
    void logAction(String action, String details);

    /**
     * Retrieves all audit logs sorted by date (newest first)
     * @return List of all audit logs
     */
    List<AuditLog> getAllAuditLogs();

    /**
     * Retrieves audit logs filtered by specific date
     * @param date The date to filter by
     * @return List of audit logs from the specified date
     */
    List<AuditLog> getAuditLogsByDate(LocalDate date);

    /**
     * Retrieves audit logs filtered by action type
     * @param action The action type to filter by
     * @return List of audit logs with the specified action
     */
    List<AuditLog> getAuditLogsByAction(String action);

    /**
     * Retrieves audit logs filtered by both date and action type
     * @param date The date to filter by
     * @param action The action type to filter by
     * @return List of filtered audit logs
     */
    List<AuditLog> getAuditLogsByDateAndAction(LocalDate date, String action);
}