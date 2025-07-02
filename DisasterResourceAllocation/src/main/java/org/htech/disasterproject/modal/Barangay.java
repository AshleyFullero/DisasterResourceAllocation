package org.htech.disasterproject.modal;

public class Barangay {
    private int id;
    private String name;
    private String locationDetails;

    public Barangay() {}

    public Barangay(int id, String name, String locationDetails) {
        this.id = id;
        this.name = name;
        this.locationDetails = locationDetails;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocationDetails() { return locationDetails; }
    public void setLocationDetails(String locationDetails) { this.locationDetails = locationDetails; }

    @Override
    public String toString() { // Useful for displaying in ComboBoxes
        return name;
    }
}