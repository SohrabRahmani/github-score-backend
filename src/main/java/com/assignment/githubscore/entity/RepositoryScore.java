package com.assignment.githubscore.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "repository_scores")
public class RepositoryScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int repoId;
    private String repoName;
    private double popularityScore;
    private LocalDateTime recordedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRepoId() {
        return repoId;
    }

    public void setRepoId(int repoId) {
        this.repoId = repoId;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public double getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(double popularityScore) {
        this.popularityScore = popularityScore;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    @Override
    public String toString() {
        return STR."""
                GithubRepoScore{
                  id=\{id}
                 ,repoId=\{repoId}
                 ,repoName='\{repoName}\{'\''}
                 ,popularityScore=\{popularityScore}
                 ,recordedAt=\{recordedAt}\{'}'}""";
    }
}
