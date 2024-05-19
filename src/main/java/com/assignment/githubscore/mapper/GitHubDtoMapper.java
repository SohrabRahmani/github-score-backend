package com.assignment.githubscore.mapper;

import com.assignment.githubscore.dto.*;
import com.assignment.githubscore.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GitHubDtoMapper {

    public RepoItemDTO toRepoItemDTO(RepoItem repoItem, double popularityScore, String trend) {
        return new RepoItemDTO(
                repoItem.id(),
                popularityScore,
                trend,
                repoItem.nodeId(),
                repoItem.name(),
                repoItem.fullName(),
                repoItem.isPrivate(),
                repoItem.htmlUrl(),
                repoItem.isFork(),
                repoItem.createdAt(),
                repoItem.updatedAt(),
                repoItem.stargazersCount(),
                repoItem.language(),
                repoItem.forksCount(),
                repoItem.archived(),
                repoItem.disabled()
        );
    }
}