package com.assignment.githubscore.mapper;

import com.assignment.githubscore.dto.UserPreferencesDTO;
import com.assignment.githubscore.entity.UserPreferences;
import org.springframework.stereotype.Component;

@Component
public class UserPreferencesMapper {

    public UserPreferencesDTO toDto(UserPreferences userPreferences) {
        return new UserPreferencesDTO(
                userPreferences.getUserId(),
                userPreferences.getStarsWeight(),
                userPreferences.getForksWeight(),
                userPreferences.getRecencyWeight()
        );
    }

    public UserPreferences toEntity(UserPreferencesDTO userPreferencesDTO) {
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setUserId(userPreferencesDTO.userId());
        userPreferences.setStarsWeight(userPreferencesDTO.starsWeight());
        userPreferences.setForksWeight(userPreferencesDTO.forksWeight());
        userPreferences.setRecencyWeight(userPreferencesDTO.recencyWeight());
        return userPreferences;
    }
}
