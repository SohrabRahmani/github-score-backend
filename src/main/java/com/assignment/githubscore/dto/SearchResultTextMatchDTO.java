package com.assignment.githubscore.dto;

import java.util.List;

public record SearchResultTextMatchDTO(
        String objectUrl,
        String objectType,
        String property,
        String fragment,
        List<TextMatchDTO> matches) {
}
