package com.pahappa.hospital.beans;

import com.pahappa.hospital.models.AuditLog;
import com.pahappa.hospital.services.auditLog.AuditLogService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class AuditLogBean implements Serializable {

    @Inject
    private AuditLogService auditLogService;

    private List<AuditLog> auditLogs;
    private LocalDate filterDate;
    private String filterAction;
    private AuditLog selectedAuditLog;

    @PostConstruct
    public void init() {
        loadAllAuditLogs();
    }

    public void loadAllAuditLogs() {
        try {
            auditLogs = auditLogService.getAllAuditLogs();
        } catch (Exception e) {
            addErrorMessage("Failed to load audit logs: " + e.getMessage());
        }
    }

    public void applyFilters() {
        try {
            if (filterDate != null && filterAction != null && !filterAction.isEmpty()) {
                auditLogs = auditLogService.getAuditLogsByDateAndAction(filterDate, filterAction);
            } else if (filterDate != null) {
                auditLogs = auditLogService.getAuditLogsByDate(filterDate);
            } else if (filterAction != null && !filterAction.isEmpty()) {
                auditLogs = auditLogService.getAuditLogsByAction(filterAction);
            } else {
                loadAllAuditLogs();
            }
        } catch (Exception e) {
            addErrorMessage("Failed to filter audit logs: " + e.getMessage());
            loadAllAuditLogs();
        }
    }

    public void clearFilters() {
        filterDate = null;
        filterAction = null;
        loadAllAuditLogs();
    }

    public void viewDetails(AuditLog auditLog) {
        selectedAuditLog = auditLog;
    }

    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    // Getters and Setters
    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public LocalDate getFilterDate() {
        return filterDate;
    }

    public void setFilterDate(LocalDate filterDate) {
        this.filterDate = filterDate;
    }

    public String getFilterAction() {
        return filterAction;
    }

    public void setFilterAction(String filterAction) {
        this.filterAction = filterAction;
    }

    public AuditLog getSelectedAuditLog() {
        return selectedAuditLog;
    }

    public void setSelectedAuditLog(AuditLog selectedAuditLog) {
        this.selectedAuditLog = selectedAuditLog;
    }
}