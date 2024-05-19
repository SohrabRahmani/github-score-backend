package com.assignment.githubscore.dto;

import java.util.List;

public record TextMatchDTO(
        String text,
        List<Integer> indices) {
}
