# ExerciseLog Backend

This repository contains the backend code for the **ExerciseLog App**, a web-based application designed to help users track, plan, and manage their exercise routines. The backend is built using **Spring Boot** and provides RESTful APIs for the frontend to interact with.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## Features

- **User Authentication**: Registration and login with JWT-based authentication.
- **Exercise Logging**: Users can log workouts with details like exercise type, duration, and intensity.
- **Planned Exercises**: Schedule future workouts and set fitness goals.
- **Progress Monitoring**: View statistics and analytics of workout data.
- **Admin Functionality**: Manage user roles and delete user accounts.

## Technologies Used

- **Java 17**
- **Spring Boot**
- **PostgreSQL**
- **JWT (JSON Web Tokens)**
- **Hibernate (JPA)**
- **Maven**

## Prerequisites

- **Java 17** or higher installed
- **Maven** installed
- **PostgreSQL** database setup
- **Git** installed (for cloning the repository)

## Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/exerciselog-backend.git
   cd exerciselog-backend
   ```

2. **Install Dependencies**

   ```bash
   mvn clean install
   ```

## Configuration

The application uses environment variables for sensitive information like database credentials and JWT secret keys. Create a `.env` file in the root directory (note that this file should be gitignored).

### `.env` File

```
SECRET_KEY=your_jwt_secret_key
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/your_database
SPRING_DATASOURCE_USERNAME=your_db_username
SPRING_DATASOURCE_PASSWORD=your_db_password
```

- Replace `your_jwt_secret_key` with a secure secret key for JWT signing.
- Update the database URL, username, and password according to your PostgreSQL setup.

## Running the Application

### With Maven

```bash
mvn spring-boot:run
```

### Packaging as a JAR

```bash
mvn clean package
java -jar target/exerciselog-app.jar
```

## API Endpoints

### Authentication

- **POST** `/api/register`: Register a new user.
- **POST** `/api/login`: Authenticate a user and receive a JWT.

### Profile

- **GET** `/api/profile`: Get current user profile.
- **POST** `/api/profile/change-password`: Change password for current user.

### Workouts

- **GET** `/api/workouts`: Get all planned workouts.
- **GET** `/api/workouts/{id}`: Get a specific planned workout.
- **POST** `/api/workouts`: Add a new planned workout.
- **PUT** `/api/workouts/{id}`: Edit an existing planned workout.
- **DELETE** `/api/workouts/delete-planned/{id}`: Delete a planned workout.
- **GET** `/api/workouts/start/{id}`: Start a workout session.
- **POST** `/api/workouts/complete/{id}`: Complete a workout session.
- **GET** `/api/workouts/completed`: Get all completed workouts.
- **DELETE** `/api/workouts/delete-completed/{id}`: Delete a completed workout.

### Planned Exercises

- **GET** `/api/planned`: Get all planned exercises.
- **GET** `/api/planned/{id}`: Get a specific planned exercise.
- **POST** `/api/planned`: Add a new planned exercise.
- **PUT** `/api/planned/{id}`: Edit a planned exercise.
- **DELETE** `/api/planned/{id}`: Delete a planned exercise.

### Exercise Logs

- **GET** `/api/logs`: Get exercise logs, with optional filtering by exercise name.

### Admin (Requires Admin Role)

- **GET** `/api/admin/users`: Get all users.
- **GET** `/api/admin/users/{id}`: Get a specific user.
- **PUT** `/api/admin/users/{id}`: Update a user's role.
- **DELETE** `/api/admin/users/{id}`: Delete a user.

## Testing

The application can be tested using tools like **Postman** or **cURL**. Ensure to include the JWT token in the `Authorization` header for protected endpoints.

Example of setting the `Authorization` header:

```
Authorization: Bearer your_jwt_token_here
```

## Deployment

The application is configured for deployment on **Render.com** but can be deployed to any cloud provider that supports Java applications.

### Environment Variables for Deployment

Ensure the following environment variables are set on your deployment platform:

- `SECRET_KEY`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.