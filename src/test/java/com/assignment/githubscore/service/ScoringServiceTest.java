package com.assignment.githubscore.service;

import com.assignment.githubscore.dto.ScoreBreakdownDTO;
import com.assignment.githubscore.dto.UserPreferencesDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        UserPreferencesDTO userPreferences = new UserPreferencesDTO(1, 0.4, 0.3, 0.3);
        when(userPreferenceService.getUserPreferences(anyLong())).thenReturn(java.util.Optional.of(userPreferences));

        LocalDateTime updatedAt = LocalDateTime.now().minusDays(10);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String updatedAtString = updatedAt.format(formatter);

        double popularityScore = scoringService.calculatePopularityScore(100, 50, updatedAtString, 1);

        assertEquals(55.027, popularityScore, 0.001);
    }

    @Test
    public void testCalculatePopularityScoreWithBreakdown() {
        UserPreferencesDTO userPreferences = new UserPreferencesDTO(1, 0.4, 0.3, 0.3);
        when(userPreferenceService.getUserPreferences(anyLong())).thenReturn(java.util.Optional.of(userPreferences));

        LocalDateTime updatedAt = LocalDateTime.now().minusDays(10);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String updatedAtString = updatedAt.format(formatter);

        ScoreBreakdownDTO scoreBreakdown = scoringService.calculatePopularityScoreWithBreakdown(100, 50, updatedAtString, 1);

        assertEquals(0.4, scoreBreakdown.starsWeight());
        assertEquals(0.3, scoreBreakdown.forksWeight());
        assertEquals(0.3, scoreBreakdown.recencyWeight());
        assertEquals(40.0, scoreBreakdown.starsFactor());
        assertEquals(15.0, scoreBreakdown.forksFactor());
        assertEquals(0.027, scoreBreakdown.recencyFactor(), 0.001);
        assertEquals(55.027, scoreBreakdown.finalScore(), 0.001);
    }
}