package com.example.AthleteTracker.models;

public class Workout {

    private String name;
    private String description;
    private String frequency;
    private int duration;
    private String[] exercises;

    public Workout(String name,
                   String description,
                   String frequency,
                   int duration,
                   String[] exercises)
    {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.duration = duration;
        this.exercises = exercises;
    }

    public Workout() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String[] getExercises() {
        return exercises;
    }

    public void setExercises(String[] exercises) {
        this.exercises = exercises;
    }
}
