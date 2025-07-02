package org.htech.disasterproject.modal;

import java.sql.Date;

public class DistributionEvent {
    public enum Status {PLANNING, IN_PROGRESS, COMPLETED}

    private int id;
    private String eventName;
    private Date eventDate;
    private Status status;
    private String notes;
    private int barangayId;

    public DistributionEvent() {
    }

    public DistributionEvent(int id, String eventName, Date eventDate, Status status, String notes, int barangayId) {
        this.id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.status = status;
        this.notes = notes;
        this.barangayId = barangayId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getBarangayId() {
        return barangayId;
    }

    public void setBarangayId(int barangayId) {
        this.barangayId = barangayId;
    }
}