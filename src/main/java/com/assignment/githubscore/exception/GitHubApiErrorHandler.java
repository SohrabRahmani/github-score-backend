package com.assignment.githubscore.exception;

public class GitHubApiErrorHandler extends RuntimeException {
    public GitHubApiErrorHandler(String message) {
        super(message);
    }
}