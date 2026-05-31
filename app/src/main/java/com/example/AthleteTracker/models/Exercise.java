package com.example.AthleteTracker.models;

public class Exercise {

    private String name;
    private String category;
    private String primaryMuscle;
    private String equipment;
    private String cnsLoad;

    public Exercise(String name,
                    String category,
                    String primaryMuscle,
                    String equipment,
                    String cnsLoad) {
        this.name = name;
        this.category = category;
        this.primaryMuscle = primaryMuscle;
        this.equipment = equipment;
        this.cnsLoad = cnsLoad;
    }

    public Exercise() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrimaryMuscle() {
        return primaryMuscle;
    }

    public void setPrimaryMuscle(String primaryMuscle) {
        this.primaryMuscle = primaryMuscle;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getCnsLoad() {
        return cnsLoad;
    }

    public void setCnsLoad(String cnsLoad) {
        this.cnsLoad = cnsLoad;
    }
}
