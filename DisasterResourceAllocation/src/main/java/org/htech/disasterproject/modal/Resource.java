package org.htech.disasterproject.modal;

public class Resource {
    private int id;
    private String name;
    private String unitType;
    private double weightKg;
    private int importanceScore;
    private int totalQuantity;
    private double valuePerWeight;

    private byte[] imageBytes;


    public Resource() {
    }

    public Resource(String name, String unitType, double weightKg, int importanceScore, int totalQuantity) {
        this.name = name;
        this.unitType = unitType;
        this.weightKg = weightKg;
        this.importanceScore = importanceScore;
        this.totalQuantity = totalQuantity;
        if (weightKg > 0) {
            this.valuePerWeight = importanceScore / weightKg;
        } else {
            this.valuePerWeight = 0;
        }
    }

    public Resource(int id, String name, String unitType, double weightKg, int importanceScore, int totalQuantity) {
        this.id = id;
        this.name = name;
        this.unitType = unitType;
        this.weightKg = weightKg;
        this.importanceScore = importanceScore;
        this.totalQuantity = totalQuantity;
        if (weightKg > 0) {
            this.valuePerWeight = importanceScore / weightKg;
        } else {
            this.valuePerWeight = 0;
        }
    }



    public Resource(String name, String unitType, Double weight, Integer importance, Integer quantity) {
        this.name = name;
        this.unitType = unitType;
        this.weightKg = weight;
        this.importanceScore = importance;
        this.totalQuantity = quantity;
        if (weightKg > 0) {
            this.valuePerWeight = importanceScore / weightKg;
        } else {
            this.valuePerWeight = 0;
        }
    }

    public Resource(String name, String unitType, double weightKg, int importanceScore, int totalQuantity, byte[] imageBytes) {
        this.name = name;
        this.unitType = unitType;
        this.weightKg = weightKg;
        this.importanceScore = importanceScore;
        this.totalQuantity = totalQuantity;
        this.imageBytes = imageBytes;
    }

    public Resource(int id, String name, String unitType, double weightKg, int importanceScore, int totalQuantity, byte[] imageBytes) {
        this.id=id;
        this.name = name;
        this.unitType = unitType;
        this.weightKg = weightKg;
        this.importanceScore = importanceScore;
        this.totalQuantity = totalQuantity;
        this.imageBytes = imageBytes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public int getImportanceScore() {
        return importanceScore;
    }

    public void setImportanceScore(int importanceScore) {
        this.importanceScore = importanceScore;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getValuePerWeight() {
        return valuePerWeight;
    }

    public void setValuePerWeight(double value) {
        this.valuePerWeight = value;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "name='" + name + '\'' +
                ", valuePerWeight=" + valuePerWeight +
                '}';
    }
}