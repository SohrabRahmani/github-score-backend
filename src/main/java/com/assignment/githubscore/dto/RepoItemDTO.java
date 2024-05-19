package com.assignment.githubscore.dto;

public record RepoItemDTO(
        int id,
        double popularityScore,
        String trend,
        String nodeId,
        String name,
        String fullName,
        boolean isPrivate,
        String htmlUrl,
        boolean isFork,
        String createdAt,
        String updatedAt,
        int stargazersCount,
        String language,
        int forksCount,
        boolean archived,
        boolean disabled
) {
}