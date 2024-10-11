package s24.backend.exerciselog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import s24.backend.exerciselog.domain.ExerciseLog;
import s24.backend.exerciselog.domain.PlannedExerciseLog;
import s24.backend.exerciselog.domain.Role;
import s24.backend.exerciselog.domain.User;
import s24.backend.exerciselog.domain.Workout;
import s24.backend.exerciselog.repository.ExerciseLogRepository;
import s24.backend.exerciselog.repository.PlannedExerciseLogRepository;
import s24.backend.exerciselog.repository.RoleRepository;
import s24.backend.exerciselog.repository.UserRepository;
import s24.backend.exerciselog.repository.WorkoutRepository;

@SpringBootApplication
public class ExerciselogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExerciselogApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(WorkoutRepository workoutRepository, UserRepository userRepository, RoleRepository roleRepository, PlannedExerciseLogRepository PlannedExerciseRepository, ExerciseLogRepository exerciseLogRepository) {
		return (args) -> {
			LocalDate date = LocalDate.now();

			List<Workout> workouts = new ArrayList<>();

			Role role = new Role("user");
			roleRepository.save(role);

			User user = new User(workouts, "username", "password", "user@email.com", role);
			userRepository.save(user);

			List<PlannedExerciseLog> plannedExerciseLogs = new ArrayList<>();
			List<ExerciseLog> exerciseLogs = new ArrayList<>();
			PlannedExerciseRepository.saveAll(plannedExerciseLogs);
			exerciseLogRepository.saveAll(exerciseLogs);

			Workout workout = new Workout(user, exerciseLogs, plannedExerciseLogs, "training", "hard", date);
			workouts.add(workout);
			workoutRepository.save(workout);
		};
	}
}
