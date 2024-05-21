# GitHub Score Backend

This project implements a backend application for scoring repositories on GitHub.

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Setup](#setup)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
   - [User Preferences](#user-preferences)
   - [GitHub Repositories](#github-repositories)
   - [Assumptions](#assumptions)
- [Configuration](#configuration)
- [Swagger Documentation](#swagger-documentation)
- [Environment Variables](#environment-variables)
- [Improvements](#improvements)
   - [Dynamic Configuration](#dynamic-configuration)
   - [Real-time Notifications](#real-time-notifications)
   - [Scalability](#scalability)

## Introduction
The GitHub Score Backend provides a RESTful API for scoring GitHub repositories based on various factors such as stars, forks, and recency of updates. It also offers additional features like comparing repository scores, user-tailored scoring mechanisms, and scoring algorithm transparency.

## Features
- Popularity score assignment for GitHub repositories
- Comparison metrics to track changes in repository scores
- User-tailored scoring mechanism to adjust scoring algorithm parameters
- Scoring algorithm transparency to provide insights into score calculation

## Requirements

- Docker
- Docker Compose

## Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/SohrabRahmani/github-score-backend.git
2. Navigate to the project directory:
    ```bash
   cd github-score-backend
3. Build the project using Maven:
    ```bash 
   mvn clean package -DskipTests
## Usage
1. Start the application:
    ```bash
   docker-compose up
2. Access the API endpoints at http://localhost:8080/api.

## API Endpoints
The GitHub Score Backend exposes the following HTTP endpoints:

### User Preferences
- `POST /api/factor/preferences`: Save user preferences for scoring algorithm factors.
- `GET /api/factor/preferences`: Get user preferences for scoring algorithm factors.
- `PUT /api/factor/preferences`: Update user preferences for scoring algorithm factors.
- `DELETE /api/factor/preferences`: Delete user preferences for scoring algorithm factors.

### GitHub Repositories
- `GET /api/github/search`: Search GitHub repositories based on language and earliest created date.
- `GET /api/github/score/breakdown`: Get the breakdown of popularity score for a repository based on stars, forks, and last updated date.

### Assumptions

- **No Security Implemented**: This application does not include any security measures. It is assumed that the `userId` passed in the header is authorized. In a production environment, proper authentication and authorization mechanisms should be implemented.

## Swagger Documentation
The API documentation for the GitHub Score Application is available using Swagger.
After starting the application, you can access the Swagger UI at http://localhost:8080/swagger-ui/index.html.
This UI provides detailed documentation of all available endpoints, request parameters, and response schemas.

## Environment Variables
The following environment variables are available for configuration:

- `DB_URL`: PostgreSQL database URL.
- `DB_NAME`: PostgreSQL database name.
- `DB_USERNAME`: PostgreSQL database username.
- `DB_PASSWORD`: PostgreSQL database password.
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Hibernate DDL auto configuration.
- `SPRING_JPA_HIBERNATE_DIALECT`: Hibernate Dialect Configuration.

## Configuration
You can configure the application using environment variables or by modifying the [application.yml](src%2Fmain%2Fresources%2Fapplication.yml) file.
See Configuration section in the source code for available options.

## Improvements
### Dynamic Configuration
Implement a mechanism to dynamically adjust scoring algorithm parameters based on usage patterns and feedback. This could involve machine learning models or user feedback mechanisms to continuously optimize the scoring algorithm.

### Real-time Notifications
Implement real-time notifications for users based on changes in repository scores or user preferences.

### Scalability

Scalability ensures the GitHub Score Backend can handle more users and interactions effectively. Here's how:

- **Horizontal Scaling**: Adding more servers to distribute the workload ensures responsiveness and reliability as user interactions grow.
- **Caching**: Storing frequently accessed data in memory reduces database load and improves response times for API requests.