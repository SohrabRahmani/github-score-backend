package com.assignment.githubscore.enums;

public enum WeightFactor {
    STARS(0.5),
    FORKS(0.3),
    RECENCY(0.2);

    private final double weight;

    WeightFactor(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}
