package com.assignment.githubscore.dto;

public record PermissionsDTO(
        boolean admin,
        boolean maintain,
        boolean push,
        boolean triage,
        boolean pull) {
}
