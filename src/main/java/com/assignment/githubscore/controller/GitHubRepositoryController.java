package com.assignment.githubscore.controller;

import com.assignment.githubscore.dto.RepoItemDTO;
import com.assignment.githubscore.dto.ScoreBreakdownDTO;
import com.assignment.githubscore.service.GitHubRepositoryService;
import com.assignment.githubscore.service.ScoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/github")
@Tag(name = "GitHub Repositories", description = "APIs for managing GitHub repositories")
public class GitHubRepositoryController {

    private final GitHubRepositoryService gitHubRepositoryService;
    private final ScoringService scoringService;

    public GitHubRepositoryController(GitHubRepositoryService gitHubRepositoryService, ScoringService scoringService) {
        this.gitHubRepositoryService = gitHubRepositoryService;
        this.scoringService = scoringService;
    }

    @Operation(summary = "Search GitHub Repository", description = "Search GitHub repositories based on language and earliest created date.")

    @GetMapping("/search")
    public ResponseEntity<List<RepoItemDTO>> searchGitHubRepository(
            @Parameter(description = "Earliest creation date of repositories (format: yyyy-MM-dd)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate earliestCreatedDate,
            @Parameter(description = "Programming language to filter repositories") @RequestParam(required = false) String language,
            @Parameter(description = "The ID of the user making the request") @RequestHeader("UserId") long userId) {
        List<RepoItemDTO> filteredRepositories = gitHubRepositoryService.getFilteredRepositories(language, earliestCreatedDate, userId);
        return ResponseEntity.ok(filteredRepositories);
    }

    @Operation(summary = "Get Score Breakdown", description = "Get the breakdown of popularity score for a repository based on stars, forks, and last updated date.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Score breakdown retrieved successfully")})
    @GetMapping("/score/breakdown")
    public ResponseEntity<ScoreBreakdownDTO> getScoreBreakdown(
            @Parameter(description = "The number of stars of the repository") @RequestParam int stars,
            @Parameter(description = "The number of forks of the repository") @RequestParam int forks,
            @Parameter(description = "The maximum number of stars for normalization") @RequestParam int maxStars,
            @Parameter(description = "The maximum number of forks for normalization") @RequestParam int maxForks,
            @Parameter(description = "The date the repository was last updated (format: yyyy-MM-dd)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedAt,
            @Parameter(description = "The earliest creation date of the repository (format: yyyy-MM-dd)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate earliestCreatedDate,
            @Parameter(description = "The ID of the user making the request") @RequestHeader("UserId") long userId) {
        ScoreBreakdownDTO scoreBreakdown = scoringService.calculatePopularityScoreWithBreakdown(
                stars, forks, maxStars, maxForks, earliestCreatedDate, updatedAt, userId);
        return ResponseEntity.ok(scoreBreakdown);
    }
}
