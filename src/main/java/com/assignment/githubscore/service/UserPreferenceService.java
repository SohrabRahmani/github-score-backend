package com.assignment.githubscore.service;

import com.assignment.githubscore.dto.UserPreferencesDTO;
import com.assignment.githubscore.entity.UserPreferences;
import com.assignment.githubscore.enums.WeightFactor;
import com.assignment.githubscore.exception.NoSuchElementException;
import com.assignment.githubscore.mapper.UserPreferencesMapper;
import com.assignment.githubscore.repository.UserPreferencesRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPreferenceService {

    private final UserPreferencesRepository userPreferencesRepository;
    private final UserPreferencesMapper userPreferencesMapper;

    public UserPreferenceService(UserPreferencesRepository userPreferencesRepository, UserPreferencesMapper userPreferencesMapper) {
        this.userPreferencesRepository = userPreferencesRepository;
        this.userPreferencesMapper = userPreferencesMapper;
    }

    /**
     * Saves user preferences to the database.
     *
     * @param userPreferencesDTO The user preferences to save.
     * @return The saved user preferences.
     */
    public UserPreferencesDTO saveUserPreferences(UserPreferencesDTO userPreferencesDTO) {
        UserPreferences userPreferences = userPreferencesMapper.toEntity(userPreferencesDTO);
        return userPreferencesMapper.toDto(userPreferencesRepository.save(userPreferences));
    }

    /**
     * Updates user preferences in the database.
     *
     * @param userPreferencesDTO The updated user preferences.
     * @return The updated user preferences.
     * @throws NoSuchElementException if the user preferences for the given user ID are not found.
     */
    public UserPreferencesDTO updateUserPreferences(UserPreferencesDTO userPreferencesDTO) {
        UserPreferences existingPreferences = getUserPreferencesById(userPreferencesDTO.userId());

        UserPreferences userPreferences = userPreferencesMapper.toEntity(userPreferencesDTO);
        userPreferences.setId(existingPreferences.getId());

        return userPreferencesMapper.toDto(userPreferencesRepository.save(userPreferences));
    }

    /**
     * Deletes user preferences from the database.
     *
     * @param userId The ID of the user whose preferences should be deleted.
     * @throws NoSuchElementException if the user preferences for the given user ID are not found.
     */
    public void deleteUserPreferences(long userId) {
        UserPreferences existingPreferences = getUserPreferencesById(userId);
        userPreferencesRepository.delete(existingPreferences);
    }

    /**
     * Retrieves user preferences from the database by user ID.
     *
     * @param userId The ID of the user whose preferences should be retrieved.
     * @return The user preferences, if found.
     */
    public Optional<UserPreferencesDTO> getUserPreferences(long userId) {
        return userPreferencesRepository.findByUserId(userId)
                .map(userPreferencesMapper::toDto);
    }

    /**
     * Generates default user preferences for the given user ID.
     *
     * @param userId The ID of the user for whom default preferences should be generated.
     * @return The default user preferences.
     */
    public UserPreferencesDTO getDefaultUserPreferences(long userId) {
        return new UserPreferencesDTO(
                userId,
                WeightFactor.STARS.getWeight(),
                WeightFactor.FORKS.getWeight(),
                WeightFactor.RECENCY.getWeight()
        );
    }

    /**
     * Retrieves user preferences from the database by user ID.
     *
     * @param userId The ID of the user whose preferences should be retrieved.
     * @return The user preferences, if found.
     * @throws NoSuchElementException if the user preferences for the given user ID are not found.
     */
    private UserPreferences getUserPreferencesById(long userId) {
        return userPreferencesRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(STR."User preferences not found for userId: \{userId}"));
    }
}