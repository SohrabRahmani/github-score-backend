package com.assignment.githubscore.service;

import com.assignment.githubscore.dto.ScoreBreakdownDTO;
import com.assignment.githubscore.dto.UserPreferencesDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScoringServiceTest {

    @Mock
    private UserPreferenceService userPreferenceService;

    @InjectMocks
    private ScoringService scoringService;


    @Test
    public void testCalculatePopularityScore() {
        int maxForks = 50;
        int maxStars = 100;

        UserPreferencesDTO userPreferences = new UserPreferencesDTO(1, 0.4, 0.3, 0.3);
        when(userPreferenceService.getUserPreferences(anyLong())).thenReturn(java.util.Optional.of(userPreferences));

        LocalDate updatedAt = LocalDate.now().minusDays(10);
        LocalDate earliestCreatedDate = LocalDate.now().minusDays(100);

        double popularityScore = scoringService.calculatePopularityScore(100, 50, updatedAt, maxStars, maxForks, earliestCreatedDate, 1);

        assertEquals(0.97, popularityScore, 0.001);
    }

    @Test
    public void testCalculatePopularityScoreWithBreakdown() {
        int maxForks = 50;
        int maxStars = 100;

        UserPreferencesDTO userPreferences = new UserPreferencesDTO(1, 0.4, 0.3, 0.3);
        when(userPreferenceService.getUserPreferences(anyLong())).thenReturn(java.util.Optional.of(userPreferences));

        LocalDate updatedAt = LocalDate.now().minusDays(10);
        LocalDate earliestCreatedDate = LocalDate.now().minusDays(10);

        ScoreBreakdownDTO scoreBreakdown = scoringService.calculatePopularityScoreWithBreakdown(
                100, 50, maxStars, maxForks, earliestCreatedDate, updatedAt, 1);

        assertEquals(0.4, scoreBreakdown.starsWeight());
        assertEquals(0.3, scoreBreakdown.forksWeight());
        assertEquals(0.3, scoreBreakdown.recencyWeight());
        assertEquals(0.4, scoreBreakdown.starsFactor());
        assertEquals(0.3, scoreBreakdown.forksFactor());
        assertEquals(0.0, scoreBreakdown.recencyFactor(), 0.001);
        assertEquals(0.7, scoreBreakdown.finalScore(), 0.001);
    }
}