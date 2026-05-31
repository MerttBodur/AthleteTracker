package com.example.AthleteTracker.models;

public class User {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatOfBirth() {
        return datOfBirth;
    }

    public void setDatOfBirth(String datOfBirth) {
        this.datOfBirth = datOfBirth;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(double bodyFat) {
        this.bodyFat = bodyFat;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getTrainingDaysPerWeek() {
        return trainingDaysPerWeek;
    }

    public void setTrainingDaysPerWeek(int trainingDaysPerWeek) {
        this.trainingDaysPerWeek = trainingDaysPerWeek;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public String getPrimaryGoal() {
        return primaryGoal;
    }

    public void setPrimaryGoal(String primaryGoal) {
        this.primaryGoal = primaryGoal;
    }

    public String getSecondaryGoal() {
        return secondaryGoal;
    }

    public void setSecondaryGoal(String secondaryGoal) {
        this.secondaryGoal = secondaryGoal;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getInjuryHistory() {
        return injuryHistory;
    }

    public void setInjuryHistory(String injuryHistory) {
        this.injuryHistory = injuryHistory;
    }

    public User() {
    }

    public User(String datOfBirth,
                String name,
                double weight,
                double height,
                double bodyFat,
                String gender,
                String sport,
                String position,
                String experience,
                int sessionDuration,
                int trainingDaysPerWeek,
                String primaryGoal,
                String secondaryGoal,
                String equipment,
                String injuryHistory) {
        this.datOfBirth = datOfBirth;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.bodyFat = bodyFat;
        this.Gender = gender;
        this.sport = sport;
        this.position = position;
        this.experience = experience;
        this.sessionDuration = sessionDuration;
        this.trainingDaysPerWeek = trainingDaysPerWeek;
        this.primaryGoal = primaryGoal;
        this.secondaryGoal = secondaryGoal;
        this.equipment = equipment;
        this.injuryHistory = injuryHistory;
    }

    private String name;
    private String datOfBirth;
    private double weight;
    private double height;
    private double bodyFat;
    private String Gender;
    private String sport;
    private String position;
    private String experience;
    private int trainingDaysPerWeek;
    private int sessionDuration;
    private String primaryGoal;
    private String secondaryGoal;
    private String equipment;
    private String injuryHistory;
}
