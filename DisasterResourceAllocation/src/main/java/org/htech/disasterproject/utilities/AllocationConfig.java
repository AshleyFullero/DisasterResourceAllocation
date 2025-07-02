package org.htech.disasterproject.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AllocationConfig {

    private static final String CONFIG_FILE = "config.properties";

    private double knapsackCapacity = 280.0;
    private boolean prioritizeInfantFamilies = true;

    private boolean guaranteeWaterPerFamily = true;
    private boolean guaranteeRicePerFamily = false;
    private boolean enableBonusForLargestFamily = true;
    private boolean distributeKitsLast = true;


    public AllocationConfig() {
        load();
    }

    public void load() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);

            this.knapsackCapacity = Double.parseDouble(props.getProperty("knapsack.capacity", "280.0"));
            this.prioritizeInfantFamilies = Boolean.parseBoolean(props.getProperty("feature.prioritizeInfants", "true"));

            this.guaranteeWaterPerFamily = Boolean.parseBoolean(props.getProperty("feature.guaranteeWater", "true"));
            this.guaranteeRicePerFamily = Boolean.parseBoolean(props.getProperty("feature.guaranteeRice", "false"));
            this.enableBonusForLargestFamily = Boolean.parseBoolean(props.getProperty("feature.bonusLargestFamily", "true"));
            this.distributeKitsLast = Boolean.parseBoolean(props.getProperty("feature.distributeKitsLast", "true"));

        } catch (IOException | NumberFormatException e) {
            System.err.println("Could not load config file, or it contains invalid data. Using default settings. Error: " + e.getMessage());
            save();
        }
    }


    public void save() {
        Properties props = new Properties();
        props.setProperty("knapsack.capacity", String.valueOf(knapsackCapacity));
        props.setProperty("feature.prioritizeInfants", String.valueOf(prioritizeInfantFamilies));

        props.setProperty("feature.guaranteeWater", String.valueOf(guaranteeWaterPerFamily));
        props.setProperty("feature.guaranteeRice", String.valueOf(guaranteeRicePerFamily));
        props.setProperty("feature.bonusLargestFamily", String.valueOf(enableBonusForLargestFamily));
        props.setProperty("feature.distributeKitsLast", String.valueOf(distributeKitsLast));

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            props.store(fos, "Disaster Relief Allocation Settings");
            System.out.println("Configuration saved successfully to " + CONFIG_FILE);
        } catch (IOException e) {
            System.err.println("Error saving configuration to file: " + e.getMessage());
        }
    }


    public double getKnapsackCapacity() {
        return knapsackCapacity;
    }

    public void setKnapsackCapacity(double knapsackCapacity) {
        this.knapsackCapacity = knapsackCapacity;
    }

    public boolean isPrioritizeInfantFamilies() {
        return prioritizeInfantFamilies;
    }

    public void setPrioritizeInfantFamilies(boolean prioritizeInfantFamilies) {
        this.prioritizeInfantFamilies = prioritizeInfantFamilies;
    }


    public boolean isGuaranteeWaterPerFamily() {
        return guaranteeWaterPerFamily;
    }

    public void setGuaranteeWaterPerFamily(boolean guaranteeWaterPerFamily) {
        this.guaranteeWaterPerFamily = guaranteeWaterPerFamily;
    }

    public boolean isGuaranteeRicePerFamily() {
        return guaranteeRicePerFamily;
    }

    public void setGuaranteeRicePerFamily(boolean guaranteeRicePerFamily) {
        this.guaranteeRicePerFamily = guaranteeRicePerFamily;
    }

    public boolean isEnableBonusForLargestFamily() {
        return enableBonusForLargestFamily;
    }

    public void setEnableBonusForLargestFamily(boolean enableBonusForLargestFamily) {
        this.enableBonusForLargestFamily = enableBonusForLargestFamily;
    }

    public boolean isDistributeKitsLast() {
        return distributeKitsLast;
    }

    public void setDistributeKitsLast(boolean distributeKitsLast) {
        this.distributeKitsLast = distributeKitsLast;
    }
}