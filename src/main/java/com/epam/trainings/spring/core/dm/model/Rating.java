package com.epam.trainings.spring.core.dm.model;

public enum Rating {

    LOW(0.8), MID(1.0), HIGH(1.2);

    private double multiplier;

    Rating(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}