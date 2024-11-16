# ExerciseLog Backend

The ExerciseLog Backend is a Spring Boot application that provides RESTful APIs for the ExerciseLog App—a web-based application designed to help users track, plan, and manage their exercise routines.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Deployment](#deployment)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Features

- **User Authentication**: Secure registration and login using JWT tokens.
- **Exercise Logging**: Log workout details, including exercise type, duration, intensity, and metrics.
- **Planned Exercises**: Schedule future workouts and set goals.
- **Progress Monitoring**: Access historical data and visualize progress.
- **Secure Backend**: Data management with PostgreSQL and secure data encryption.

## Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **PostgreSQL**
- **Hibernate (JPA)**
- **MapStruct**
- **Maven**
- **Docker (optional for containerization)**

## Prerequisites

- **Java 17** or higher
- **Maven** installed
- **PostgreSQL** database
- **Git** (for cloning the repository)

## Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/p-lemonish/exerciselog-app-backend.git
   cd exerciselog-app-backend
   ```

2. **Set Up the Database**

   - Install PostgreSQL if not already installed.
   - Create a new PostgreSQL database:

     ```sql
     CREATE DATABASE exerciselog;
     ```

3. **Install Dependencies**

   ```bash
   mvn clean install
   ```

## Configuration

The application requires certain environment variables to be set for proper functioning. These variables are loaded from a `.env` file located in the project's root directory. Create a `.env` file with the following content:

```env
SECRET_KEY=your_jwt_secret_key
ENCRYPTION_SECRET=your_encryption_secret
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/exerciselog
SPRING_DATASOURCE_USERNAME=your_db_username
SPRING_DATASOURCE_PASSWORD=your_db_password
```

- **SECRET_KEY**: A secret key used for JWT token signing.
- **ENCRYPTION_SECRET**: A secret key used for data encryption.
- **SPRING_DATASOURCE_URL**: JDBC URL for the PostgreSQL database.
- **SPRING_DATASOURCE_USERNAME**: Database username.
- **SPRING_DATASOURCE_PASSWORD**: Database password.

**Note**: Ensure that the `.env` file is included in your `.gitignore` to prevent sensitive information from being committed to version control.

## Running the Application

1. **Build the Project**

   ```bash
   mvn clean package
   ```

2. **Run the Application**

   ```bash
   mvn spring-boot:run
   ```

   The backend server will start on `http://localhost:8080`.

## Testing

To run the tests, execute:

```bash
mvn test
```

## Deployment

The backend can be deployed to any platform that supports Java applications, such as Heroku, AWS, or Render.com.

### Docker Deployment (Optional)

1. **Build Docker Image**

   ```bash
   docker build -t exerciselog-app-backend .
   ```

2. **Run Docker Container**

   ```bash
   docker run -p 8080:8080 --env-file .env exerciselog-app-backend
   ```

## API Endpoints

### Authentication

- **POST** `/api/register`: Register a new user.
- **POST** `/api/login`: Authenticate a user and receive a JWT token.

### User Profile

- **GET** `/api/profile`: Get the authenticated user's profile.
- **POST** `/api/profile/change-password`: Change the user's password.

### Workouts

- **GET** `/api/workouts`: Retrieve all planned workouts.
- **GET** `/api/workouts/{id}`: Retrieve a specific workout by ID.
- **POST** `/api/workouts`: Add a new workout.
- **PUT** `/api/workouts/{id}`: Edit an existing workout.
- **DELETE** `/api/workouts/delete-planned/{id}`: Delete a planned workout.
- **GET** `/api/workouts/start/{id}`: Start a workout.
- **POST** `/api/workouts/complete/{id}`: Complete a workout.
- **DELETE** `/api/workouts/delete-completed/{id}`: Delete a completed workout.

### Exercises

- **GET** `/api/planned`: Retrieve all planned exercises.
- **GET** `/api/planned/{id}`: Retrieve a specific planned exercise by ID.
- **POST** `/api/planned`: Add a new planned exercise.
- **PUT** `/api/planned/{id}`: Edit a planned exercise.
- **DELETE** `/api/planned/{id}`: Delete a planned exercise.

### Exercise Logs

- **GET** `/api/logs`: Retrieve exercise logs.
- **GET** `/api/logs?exerciseName={name}`: Retrieve exercise logs filtered by exercise name.

### Admin (Requires Admin Role)

- **GET** `/api/admin/users`: Retrieve all users.
- **GET** `/api/admin/users/{id}`: Retrieve a specific user by ID.
- **PUT** `/api/admin/users/{id}`: Update a user's role.
- **DELETE** `/api/admin/users/{id}`: Delete a user.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch: `git checkout -b feature/your-feature-name`.
3. Commit your changes: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin feature/your-feature-name`.
5. Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Note**: Ensure that all environment variables are correctly set and that sensitive information is not exposed. Always follow best practices for security, especially when dealing with authentication and encryption.