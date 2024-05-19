package com.assignment.githubscore.controller;

import com.assignment.githubscore.dto.UserPreferencesDTO;
import com.assignment.githubscore.service.UserPreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/factor/preferences")
@Tag(name = "User Preferences", description = "APIs for managing user preferences")
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    @Operation(summary = "Save User Preferences", description = "Save user preferences for scoring algorithm factors.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User preferences saved successfully")})
    @PostMapping
    public ResponseEntity<UserPreferencesDTO> saveUserPreferences(
            @RequestBody UserPreferencesDTO userPreferencesDTO,
            @RequestHeader("userId") long userId) {
        userPreferencesDTO = new UserPreferencesDTO(
                userId,
                userPreferencesDTO.starsWeight(),
                userPreferencesDTO.forksWeight(),
                userPreferencesDTO.recencyWeight()
        );

        return ResponseEntity.ok().body(userPreferenceService.saveUserPreferences(userPreferencesDTO));
    }

    @Operation(summary = "Get User Preferences", description = "Get user preferences for scoring algorithm factors.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User preferences retrieved successfully")})
    @GetMapping
    public ResponseEntity<UserPreferencesDTO> getUserPreferences(
            @RequestHeader("userId") Long userId) {
        UserPreferencesDTO userPreferencesDTO = userPreferenceService.getUserPreferences(userId)
                .orElseGet(() -> userPreferenceService.getDefaultUserPreferences(userId));
        return ResponseEntity.ok(userPreferencesDTO);
    }

    @Operation(summary = "Update User Preferences", description = "Update user preferences for scoring algorithm factors.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User preferences updated successfully")})
    @PutMapping
    public ResponseEntity<UserPreferencesDTO> updateUserPreferences(
            @RequestBody UserPreferencesDTO userPreferencesDTO,
            @RequestHeader("userId") long userId) {
        userPreferencesDTO = new UserPreferencesDTO(
                userId,
                userPreferencesDTO.starsWeight(),
                userPreferencesDTO.forksWeight(),
                userPreferencesDTO.recencyWeight()
        );
        return ResponseEntity.ok().body(userPreferenceService.updateUserPreferences(userPreferencesDTO));
    }

    @Operation(summary = "Delete User Preferences", description = "Delete user preferences for scoring algorithm factors.")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "User preferences deleted successfully")})
    @DeleteMapping
    public ResponseEntity<Void> deleteUserPreferences(@RequestHeader long userId) {
        userPreferenceService.deleteUserPreferences(userId);
        return ResponseEntity.noContent().build();
    }
}
