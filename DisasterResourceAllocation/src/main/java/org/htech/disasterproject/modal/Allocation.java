package org.htech.disasterproject.modal;

public class Allocation {
    private int id;
    private int distributionEventId;
    private int familyId;
    private int resourceId;
    private int quantityAllocated;

    public Allocation() {
    }

    public Allocation(int id, int distributionEventId, int familyId, int resourceId, int quantityAllocated) {
        this.id = id;
        this.distributionEventId = distributionEventId;
        this.familyId = familyId;
        this.resourceId = resourceId;
        this.quantityAllocated = quantityAllocated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistributionEventId() {
        return distributionEventId;
    }

    public void setDistributionEventId(int eventId) {
        this.distributionEventId = eventId;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getQuantityAllocated() {
        return quantityAllocated;
    }

    public void setQuantityAllocated(int quantity) {
        this.quantityAllocated = quantity;
    }
}