package com.assignment.githubscore.dto;

public record UserPreferencesDTO(
        long userId,
        double starsWeight,
        double forksWeight,
        double recencyWeight
) {
}
