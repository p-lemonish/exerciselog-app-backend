package s24.backend.exerciselog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.cdimascio.dotenv.Dotenv;
import s24.backend.exerciselog.domain.*;
import s24.backend.exerciselog.repository.*;

@SpringBootApplication
public class ExerciselogApplication {

	static{
		/*
		 * Get SECRET_KEY for Jwts from a secure, .gitignored .env-file
		 * 
		 * My .env is being loaded from directory before src for some reason
		 * 
		 * Using a static-block because otherwise the SECRET_KEY won't be 
		 * loaded when Maven does it's test run during mvn clean install
		 */
		Dotenv dotenv = Dotenv.configure()
			.ignoreIfMissing()
			.load();
		
		String secretKey = dotenv.get("SECRET_KEY");
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("SECRET_KEY was not found in the .env file");
        }
		System.setProperty("SECRET_KEY", secretKey);
	}	

	public static void main(String[] args) {
		SpringApplication.run(ExerciselogApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(PasswordEncoder passwordEncoder, ExerciseRepository exerciseRepository, WorkoutRepository workoutRepository, UserRepository userRepository, RoleRepository roleRepository, PlannedExerciseLogRepository PlannedExerciseRepository, ExerciseLogRepository exerciseLogRepository) {
		return (args) -> {
			List<PlannedExerciseLog> plannedExerciseLogs = new ArrayList<>();

			Role roleUser = new Role("USER");
			Role roleAdmin = new Role("ADMIN");
			Role rolePaidUser = new Role("PAIDUSER");
			Role roleTrialUser = new Role("TRIALUSER");
			roleRepository.save(roleUser);
			roleRepository.save(roleAdmin);
			roleRepository.save(rolePaidUser);
			roleRepository.save(roleTrialUser);

			String encodedPassword = passwordEncoder.encode("pass");
			String encodedPassword2 = passwordEncoder.encode("admin");
			User user = new User(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), roleUser, "user", encodedPassword, "user@email.com");
			User admin = new User(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), roleAdmin, "admin", encodedPassword2, "admin@email.com");

			userRepository.save(user);
			userRepository.save(admin);

			Exercise squatExercise = new Exercise("Squat", "Legs");
			Exercise benchExercise = new Exercise("Bench", "Chest");
			Exercise deadliftExercise = new Exercise("Deadlift", "Full body");
			Exercise pullupExercise = new Exercise("Pull-up", "Back");
			Exercise shoulderPressExercise = new Exercise("Shoulder press", "Shoulders");
			Exercise bentOverRowExercise = new Exercise("Bent over row", "Back");
			exerciseRepository.save(squatExercise);
			exerciseRepository.save(benchExercise);
			exerciseRepository.save(deadliftExercise);
			exerciseRepository.save(pullupExercise);
			exerciseRepository.save(shoulderPressExercise);
			exerciseRepository.save(bentOverRowExercise);

			PlannedExerciseLog plannedExerciseLogSquat = new PlannedExerciseLog(squatExercise, user, 3, 8, 110, "");
			PlannedExerciseLog plannedExerciseLogBench = new PlannedExerciseLog(benchExercise, user, 3, 8, 90, "");
			PlannedExerciseLog plannedExerciseLogDeadlift = new PlannedExerciseLog(deadliftExercise, user, 3, 5, 140, "");
			PlannedExerciseLog plannedExerciseLogPullup = new PlannedExerciseLog(pullupExercise, user, 3, 8, 100, "Bodyweight + added weight");
			PlannedExerciseLog plannedExerciseLogShoulderPress = new PlannedExerciseLog(shoulderPressExercise, user, 3, 8, 60, "");
			PlannedExerciseLog plannedExerciseLogBentOverRow = new PlannedExerciseLog(bentOverRowExercise, user, 3, 8, 85, "");
			plannedExerciseLogs.add(plannedExerciseLogSquat);
			plannedExerciseLogs.add(plannedExerciseLogBench);
			plannedExerciseLogs.add(plannedExerciseLogDeadlift);
			plannedExerciseLogs.add(plannedExerciseLogPullup);
			plannedExerciseLogs.add(plannedExerciseLogShoulderPress);
			plannedExerciseLogs.add(plannedExerciseLogBentOverRow);

			PlannedExerciseRepository.saveAll(plannedExerciseLogs);
			user.getPlannedExerciseLogs().addAll(plannedExerciseLogs);

			userRepository.save(user);
		};
	}
}
