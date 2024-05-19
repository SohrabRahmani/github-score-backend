package com.assignment.githubscore.dto;

public record ScoreBreakdownDTO(
        double starsWeight,
        double forksWeight,
        double recencyWeight,
        double starsFactor,
        double forksFactor,
        double recencyFactor,
        double finalScore
) {
}
