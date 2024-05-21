package com.assignment.githubscore.service;

import com.assignment.githubscore.dto.UserPreferencesDTO;
import com.assignment.githubscore.entity.UserPreferences;
import com.assignment.githubscore.exception.NoSuchElementException;
import com.assignment.githubscore.mapper.UserPreferencesMapper;
import com.assignment.githubscore.repository.UserPreferencesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPreferenceServiceTest {

    @Mock
    private UserPreferencesRepository userPreferencesRepository;

    @Mock
    private UserPreferencesMapper userPreferencesMapper;

    @InjectMocks
    private UserPreferenceService userPreferenceService;

    @Test
    void saveUserPreferences() {
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO(1L, 0.4, 0.3, 0.3);
        UserPreferences userPreferences = new UserPreferences();
        when(userPreferencesMapper.toEntity(userPreferencesDTO)).thenReturn(userPreferences);
        when(userPreferencesRepository.save(userPreferences)).thenReturn(userPreferences);
        when(userPreferencesMapper.toDto(userPreferences)).thenReturn(userPreferencesDTO);

        UserPreferencesDTO savedPreferences = userPreferenceService.saveUserPreferences(userPreferencesDTO);

        assertEquals(userPreferencesDTO, savedPreferences);
        verify(userPreferencesRepository, times(1)).save(userPreferences);
    }

    @Test
    void updateUserPreferences() {
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO(1L, 0.4, 0.3, 0.3);
        UserPreferences existingPreferences = new UserPreferences();
        when(userPreferencesRepository.findByUserId(anyLong())).thenReturn(Optional.of(existingPreferences));
        UserPreferences updatedPreferences = new UserPreferences();
        when(userPreferencesMapper.toEntity(userPreferencesDTO)).thenReturn(updatedPreferences);
        when(userPreferencesRepository.save(updatedPreferences)).thenReturn(updatedPreferences);
        when(userPreferencesMapper.toDto(updatedPreferences)).thenReturn(userPreferencesDTO);

        UserPreferencesDTO updated = userPreferenceService.updateUserPreferences(userPreferencesDTO);

        assertEquals(userPreferencesDTO, updated);
        verify(userPreferencesRepository, times(1)).save(updatedPreferences);
    }

    @Test
    void updateUserPreferences_NotFound() {
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO(1L, 0.4, 0.3, 0.3);
        when(userPreferencesRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> userPreferenceService.updateUserPreferences(userPreferencesDTO));
    }

    @Test
    void deleteUserPreferences() {
        UserPreferences existingPreferences = new UserPreferences();
        existingPreferences.setId(1L);
        existingPreferences.setUserId(1L);

        when(userPreferencesRepository.findByUserId(1L)).thenReturn(Optional.of(existingPreferences));

        userPreferenceService.deleteUserPreferences(1L);

        verify(userPreferencesRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUserPreferences_NotFound() {
        when(userPreferencesRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> userPreferenceService.deleteUserPreferences(1L));
    }

    @Test
    void getUserPreferences() {
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO(1L, 0.4, 0.3, 0.3);
        UserPreferences userPreferences = new UserPreferences();
        when(userPreferencesRepository.findByUserId(anyLong())).thenReturn(Optional.of(userPreferences));
        when(userPreferencesMapper.toDto(userPreferences)).thenReturn(userPreferencesDTO);

        Optional<UserPreferencesDTO> fetched = userPreferenceService.getUserPreferences(1L);

        assertEquals(userPreferencesDTO, fetched.orElse(null));
    }

    @Test
    void getDefaultUserPreferences() {
        long userId = 1L;
        UserPreferencesDTO expected = new UserPreferencesDTO(userId, 0.5, 0.3, 0.2); // Example weights

        UserPreferencesDTO defaultPreferences = userPreferenceService.getDefaultUserPreferences(userId);

        assertEquals(expected, defaultPreferences);
    }
}
