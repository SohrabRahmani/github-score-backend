package com.assignment.githubscore.model;

import java.util.List;

public record SearchResponse(
        int totalCount,
        boolean incompleteResults,
        List<RepoItem> items) {
}
