package com.assignment.githubscore.service;

import com.assignment.githubscore.dto.ScoreBreakdownDTO;
import com.assignment.githubscore.dto.UserPreferencesDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Service class for calculating GitHub repository popularity scores.
 */
@Service
public class ScoringService {

    private final UserPreferenceService userPreferenceService;

    private static final int ROUNDING_FACTOR = 100;

    public ScoringService(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    /**
     * Calculates the popularity score of a repository based on stars, forks, and recency of updates.
     *
     * @param stars               the number of stars for the repository
     * @param forks               the number of forks for the repository
     * @param updatedAt           the date and time when the repository was last updated
     * @param maxStars            the max stars for a repository
     * @param maxForks            the max fork for a repository
     * @param earliestCreatedDate the earliest date when repositories were created.
     * @param userId              the ID of the user for whom the score is calculated
     * @return the popularity score of the repository
     */
    public double calculatePopularityScore(int stars, int forks, LocalDate updatedAt, int maxStars, int maxForks, LocalDate earliestCreatedDate, long userId) {
        UserPreferencesDTO userPreferences = getUserPreferences(userId);
        long recency = calculateRecency(updatedAt);
        double normalizeStars = normalizeStars(stars, maxStars);
        double normalizeForks = normalizeForks(forks, maxForks);
        double normalizeRecency = normalizeRecency(recency, earliestCreatedDate);

        return calculateScore(normalizeStars, normalizeForks, normalizeRecency, userPreferences);
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
    public ScoreBreakdownDTO calculatePopularityScoreWithBreakdown(
            int stars, int forks, int maxStars, int maxForks,
            LocalDate earliestCreatedDate,
            LocalDate updatedAt, long userId) {
        UserPreferencesDTO userPreferences = getUserPreferences(userId);
        long recency = calculateRecency(updatedAt);
        double normalizeStars = normalizeStars(stars, maxStars);
        double normalizeForks = normalizeForks(forks, maxForks);
        double normalizeRecency = normalizeRecency(recency, earliestCreatedDate);

        return calculateScoreWithBreakdown(normalizeStars, normalizeForks, normalizeRecency, userPreferences);
    }

    /**
     * Calculates the score with breakdown based on stars, forks, and recency.
     *
     * @param stars       the number of stars
     * @param forks       the number of forks
     * @param recency     the normalized recency value between 0 and 1, where 1 represents the least cost (most recent update)
     * @param preferences the user preferences for weighting the factors
     * @return the score breakdown DTO containing weighted factors and calculated scores
     */
    private ScoreBreakdownDTO calculateScoreWithBreakdown(double stars, double forks, double recency, UserPreferencesDTO preferences) {
        double starsFactor = stars * preferences.starsWeight();
        double forksFactor = forks * preferences.forksWeight();
        double recencyFactor = recency * preferences.recencyWeight();
        double score = starsFactor + forksFactor + recencyFactor;

        return new ScoreBreakdownDTO(
                preferences.starsWeight(),
                preferences.forksWeight(),
                preferences.recencyWeight(),
                roundToTwoDecimals(starsFactor),
                roundToTwoDecimals(forksFactor),
                roundToTwoDecimals(recencyFactor),
                roundToTwoDecimals(score)
        );
    }

    private double calculateScore(double stars, double forks, double recency, UserPreferencesDTO userPreferences) {
        double starsFactor = stars * userPreferences.starsWeight();
        double forksFactor = forks * userPreferences.forksWeight();
        double recencyFactor = recency * userPreferences.recencyWeight();
        double score = starsFactor + forksFactor + recencyFactor;

        return roundToTwoDecimals(score);
    }

    private static double roundToTwoDecimals(double value) {
        return Math.round(value * ROUNDING_FACTOR) / (double) ROUNDING_FACTOR;
    }

    private long calculateRecency(LocalDate updatedAt) {
        return ChronoUnit.DAYS.between(updatedAt, LocalDate.now());
    }

    public double normalizeStars(int stars, int maxStars) {
        return stars / (double) maxStars;
    }

    public double normalizeForks(int forks, int maxForks) {
        return forks / (double) maxForks;
    }

    public double normalizeRecency(double recency, LocalDate earliestCreatedDate) {
        long totalDuration = calculateTotalDuration(earliestCreatedDate);

        if (totalDuration == 0) {
            return 1.0;
        }

        return 1 - (recency / totalDuration);
    }

    private long calculateTotalDuration(LocalDate earliestCreatedDate) {
        return ChronoUnit.DAYS.between(earliestCreatedDate, LocalDate.now());
    }

    private UserPreferencesDTO getUserPreferences(long userId) {
        return userPreferenceService.getUserPreferences(userId)
                .orElseGet(() -> userPreferenceService.getDefaultUserPreferences(userId));
    }
}