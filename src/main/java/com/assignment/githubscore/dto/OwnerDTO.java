package com.assignment.githubscore.dto;

public record OwnerDTO(
        String name,
        String email,
        String login,
        int id,
        String nodeId,
        String avatarUrl,
        String gravatarId,
        String url,
        String htmlUrl,
        String followersUrl,
        String followingUrl,
        String gistsUrl,
        String starredUrl,
        String subscriptionsUrl,
        String organizationsUrl,
        String reposUrl,
        String eventsUrl,
        String receivedEventsUrl,
        String type,
        boolean siteAdmin,
        String starredAt) {
}