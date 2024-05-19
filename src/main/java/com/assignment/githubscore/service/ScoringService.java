package com.assignment.githubscore.service;

import com.assignment.githubscore.dto.ScoreBreakdownDTO;
import com.assignment.githubscore.dto.UserPreferencesDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Service class for calculating GitHub repository popularity scores.
 */
@Service
public class ScoringService {

    private final UserPreferenceService userPreferenceService;

    private static final int ROUNDING_FACTOR = 1000;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public ScoringService(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    /**
     * Calculates the popularity score of a repository based on stars, forks, and recency of updates.
     *
     * @param stars     the number of stars for the repository
     * @param forks     the number of forks for the repository
     * @param updatedAt the date and time when the repository was last updated
     * @param userId    the ID of the user for whom the score is calculated
     * @return the popularity score of the repository
     */
    public double calculatePopularityScore(int stars, int forks, String updatedAt, long userId) {
        LocalDateTime lastUpdated = LocalDateTime.parse(updatedAt, DATE_TIME_FORMATTER);
        LocalDateTime currentTime = LocalDateTime.now();

        double recencyFactor = calculateRecencyFactor(lastUpdated, currentTime);
        UserPreferencesDTO userPreferences = getUserPreferences(userId);

        return calculateScore(stars, forks, recencyFactor, userPreferences);
    }

    /**
     * Calculates the popularity score of a repository with a breakdown of individual factors.
     *
     * @param stars     the number of stars for the repository
     * @param forks     the number of forks for the repository
     * @param updatedAt the date and time when the repository was last updated
     * @param userId    the ID of the user for whom the score is calculated
     * @return the breakdown of the popularity score
     */
    public ScoreBreakdownDTO calculatePopularityScoreWithBreakdown(int stars, int forks, String updatedAt, long userId) {
        LocalDateTime lastUpdated = LocalDateTime.parse(updatedAt, DATE_TIME_FORMATTER);
        LocalDateTime currentTime = LocalDateTime.now();

        double recencyFactor = calculateRecencyFactor(lastUpdated, currentTime);
        UserPreferencesDTO userPreferences = getUserPreferences(userId);

        return calculateScoreWithBreakdown(stars, forks, recencyFactor, userPreferences);
    }

    private UserPreferencesDTO getUserPreferences(long userId) {
        return userPreferenceService.getUserPreferences(userId)
                .orElseGet(() -> userPreferenceService.getDefaultUserPreferences(userId));
    }

    private ScoreBreakdownDTO calculateScoreWithBreakdown(int stars, int forks, double recency, UserPreferencesDTO preferences) {
        double starsFactor = stars * preferences.starsWeight();
        double forksFactor = forks * preferences.forksWeight();
        double recencyFactor = recency * preferences.recencyWeight();
        double score = starsFactor + forksFactor + recencyFactor;

        return new ScoreBreakdownDTO(
                preferences.starsWeight(),
                preferences.forksWeight(),
                preferences.recencyWeight(),
                starsFactor,
                forksFactor,
                recencyFactor,
                roundToThreeDecimals(score)
        );
    }

    private double calculateRecencyFactor(LocalDateTime lastUpdated, LocalDateTime currentTime) {
        long daysSinceLastUpdate = ChronoUnit.DAYS.between(lastUpdated, currentTime);
        double recencyFactor = 1.0 / (1.0 + daysSinceLastUpdate);
        return roundToThreeDecimals(recencyFactor);
    }

    private double calculateScore(int stars, int forks, double recency, UserPreferencesDTO userPreferences) {
        double starsFactor = stars * userPreferences.starsWeight();
        double forksFactor = forks * userPreferences.forksWeight();
        double recencyFactor = recency * userPreferences.recencyWeight();
        double score = starsFactor + forksFactor + recencyFactor;

        return roundToThreeDecimals(score);
    }

    private double roundToThreeDecimals(double value) {
        return Math.round(value * ROUNDING_FACTOR) / (double) ROUNDING_FACTOR;
    }
}
