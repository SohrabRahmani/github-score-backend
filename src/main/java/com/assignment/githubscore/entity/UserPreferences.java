package com.assignment.githubscore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private double starsWeight;
    private double forksWeight;
    private double recencyWeight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getStarsWeight() {
        return starsWeight;
    }

    public void setStarsWeight(double starsWeight) {
        this.starsWeight = starsWeight;
    }

    public double getForksWeight() {
        return forksWeight;
    }

    public void setForksWeight(double forksWeight) {
        this.forksWeight = forksWeight;
    }

    public double getRecencyWeight() {
        return recencyWeight;
    }

    public void setRecencyWeight(double recencyWeight) {
        this.recencyWeight = recencyWeight;
    }

    @Override
    public String toString() {
        return STR."""
        UserPreferences{
            id=\{id}
            ,userId=\{userId}
            ,starsWeight=\{starsWeight}
            ,forksWeight=\{forksWeight}
            ,recencyWeight=\{recencyWeight}
        }
        """;
    }
}
