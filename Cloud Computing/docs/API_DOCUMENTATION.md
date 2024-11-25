# API Documentation

## Table of Contents
- [Authentication](#authentication)
- [User Management](#user-management)
- [Animal Data](#animal-data)
- [Quiz Data](#quiz-data)
- [Media Management](#media-management)
- [User Statistics (Optional)](#user-statistics-optional)

---

## Authentication
### POST `/api/auth/register`
- **Description**: Registers a new user.
- **Request Body**:
  ```json
  {
    "username": "string",
    "email": "string",
    "password": "string"
  }
- **Response**: Returns new user info or an error message.

### POST `/api/auth/login`
- **Description**: Logs in a user and provides an authentication token.
- **Request Body**:
  ```json
  {
    "email": "string",
    "password": "string"
  }
- **Response**: Returns a token for user authentication.

## User Management
### GET `/api/users/:userId`
- **Description**: Fetches information of a specific user.
- **Response**: Returns user data based on userId.

### PUT `/api/users/:userId`
- **Description**: Updates user information.
- **Request Body**:
  ```json
  {
    "email": "string",
    "password": "string"
  }
- **Response**: Returns updated user data.

### DELETE `/api/users/:userId`
- **Description**: Deletes a user by userId.
- **Response**: Returns confirmation of user deletion.

## Animal Data
### GET `/api/animals`
- **Description**: Retrieves a list of all animals.
- **Response**: Returns an array of animals with fields like name, description, facts, image URL, and audio URL.

### GET `/api/animals/:animalId`
- **Description**: Fetches specific animal details by animalId.
- **Response**: Returns detailed animal data including name, description, facts, image URL, and audio URL.

## Animal Data
### GET `/api/quizzes`
- **Description**: Retrieves all quizzes.
- **Response**: Returns an array of quiz data including question, answer options, and correct answer.

### GET `/api/quizzes/:quizId`
- **Description**: Fetches specific quiz details by quizId.
- **Response**: Returns quiz details including question, options, and correct answer.

## Media Management
### POST `/api/media/upload`
- **Description**: Uploads an animal image or audio file to Cloud Storage.
- **Request**: Form Data: file - The image or audio file to upload.
- **Response**: Returns URL of the uploaded file in Cloud Storage.

### DELETE `/api/media/:fileName`
- **Description**: Deletes media from Cloud Storage by file name.
- **Response**: Returns confirmation of file deletion.

## User Statistics (Optional)
### POST `/api/stats/record`
- **Description**: Records quiz results or other user statistics.
- **Request Body**: 
  ```json
  {
    "userId": "string",
    "quizId": "string",
    "score": "number"
  }
- **Response**: Returns confirmation of recorded statistics.

### GET `/api/stats/:userId`
- **Description**: Fetches quiz scores or statistics for a specific user by userId.
- **Response**: Returns user's quiz statistics or scores.
