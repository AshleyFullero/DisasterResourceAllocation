package org.htech.disasterproject.modal;

public class Family {
    private int id;
    private String familyHeadName;
    private int familySize;
    private String address;
    private String notes;
    private int barangayId;

    private String barangayName;

    private double priorityScore;
    private byte[] imageBytes;



    public Family() {}

    public Family(int id, String familyHeadName, int familySize, String address, String notes, int barangayId) {
        this.id = id;
        this.familyHeadName = familyHeadName;
        this.familySize = familySize;
        this.address = address;
        this.notes = notes;
        this.barangayId = barangayId;
    }

    public Family(String headName, Integer familySize, String address, String notes, int barangayId) {
        this.familyHeadName = headName;
        this.familySize = familySize;
        this.address = address;
        this.notes = notes;
        this.barangayId = barangayId;
    }

    public Family(String familyHeadName, int familySize, String address, String notes, int barangayId, byte[] imageBytes) {
        this.familyHeadName = familyHeadName;
        this.familySize = familySize;
        this.address = address;
        this.notes = notes;
        this.barangayId = barangayId;
        this.imageBytes = imageBytes;
    }

    public Family(int id, String headName, int familySize, String address, String notes, int barangayId, byte[] selectedImageBytes) {
        this.id = id;
        this.familyHeadName = headName;
        this.familySize = familySize;
        this.address = address;
        this.notes = notes;
        this.barangayId = barangayId;
        this.imageBytes = selectedImageBytes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFamilyHeadName() { return familyHeadName; }
    public void setFamilyHeadName(String name) { this.familyHeadName = name; }

    public int getFamilySize() {
        return familySize;
    }

    public void setFamilySize(int familySize) {
        this.familySize = familySize;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getBarangayName() {
        return barangayName;
    }

    public void setBarangayName(String barangayName) {
        this.barangayName = barangayName;
    }

    public double getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(double priorityScore) {
        this.priorityScore = priorityScore;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    @Override
    public String toString() {
        return  notes;
    }
}