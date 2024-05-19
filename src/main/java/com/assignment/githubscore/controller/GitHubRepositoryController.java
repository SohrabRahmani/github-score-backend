package com.assignment.githubscore.controller;

import com.assignment.githubscore.dto.RepoItemDTO;
import com.assignment.githubscore.dto.ScoreBreakdownDTO;
import com.assignment.githubscore.service.GitHubRepositoryService;
import com.assignment.githubscore.service.ScoringService;
import io.swagger.v3.oas.annotations.Operation;
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
            @RequestParam(required = false) String language,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate earliestCreatedDate,
            @RequestHeader("UserId") long userId) {
        List<RepoItemDTO> filteredRepositories = gitHubRepositoryService.getFilteredRepositories(language, earliestCreatedDate, userId);
        return ResponseEntity.ok(filteredRepositories);
    }

    @Operation(summary = "Get Score Breakdown", description = "Get the breakdown of popularity score for a repository based on stars, forks, and last updated date.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Score breakdown retrieved successfully")})
    @GetMapping("/score/breakdown")
    public ResponseEntity<ScoreBreakdownDTO> getScoreBreakdown(
            @RequestParam int stars,
            @RequestParam int forks,
            @RequestParam String updatedAt,
            @RequestHeader("UserId") long userId) {
        ScoreBreakdownDTO scoreBreakdown = scoringService.calculatePopularityScoreWithBreakdown(stars, forks, updatedAt, userId);
        return ResponseEntity.ok(scoreBreakdown);
    }
}
