package s24.backend.exerciselog;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.cdimascio.dotenv.Dotenv;
import s24.backend.exerciselog.domain.entity.CompletedWorkout;
import s24.backend.exerciselog.domain.entity.ExerciseLog;
import s24.backend.exerciselog.domain.entity.PlannedExerciseLog;
import s24.backend.exerciselog.domain.entity.Role;
import s24.backend.exerciselog.domain.entity.User;
import s24.backend.exerciselog.domain.entity.Workout;
import s24.backend.exerciselog.repository.*;
import s24.backend.exerciselog.util.SecurityUtils;

@SpringBootApplication
public class ExerciselogApplication {

	static{
		/*
		 * Get SECRET_KEY for Jwts (and other variables) from a secure, .gitignored .env-file
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

		String encryptionSecret = dotenv.get("ENCRYPTION_SECRET");
		if (encryptionSecret == null || encryptionSecret.isEmpty()) {
			throw new IllegalStateException("ENCRYPTION_SECRET was not found in the .env file");
		}
		System.setProperty("ENCRYPTION_SECRET", encryptionSecret);

		String datasourceUrl = dotenv.get("SPRING_DATASOURCE_URL");
        if (datasourceUrl == null || datasourceUrl.isEmpty()) {
            throw new IllegalStateException("SPRING_DATASOURCE_URL was not found in the .env file");
        }
        System.setProperty("SPRING_DATASOURCE_URL", datasourceUrl);

        String datasourceUsername = dotenv.get("SPRING_DATASOURCE_USERNAME");
        if (datasourceUsername == null || datasourceUsername.isEmpty()) {
            throw new IllegalStateException("SPRING_DATASOURCE_USERNAME was not found in the .env file");
        }
        System.setProperty("SPRING_DATASOURCE_USERNAME", datasourceUsername);

        String datasourcePassword = dotenv.get("SPRING_DATASOURCE_PASSWORD");
        if (datasourcePassword == null || datasourcePassword.isEmpty()) {
            throw new IllegalStateException("SPRING_DATASOURCE_PASSWORD was not found in the .env file");
        }
        System.setProperty("SPRING_DATASOURCE_PASSWORD", datasourcePassword);
	}	

	public static void main(String[] args) {
		SpringApplication.run(ExerciselogApplication.class, args);
	}

	@SuppressWarnings("unused")
	@Bean
	public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return (args) -> {
			// Initialize USER and ADMIN roles in database
			Role userRole = roleRepository.findByName("USER")
				.orElseGet(() -> roleRepository.save(new Role("USER")));
			Role paidUserRole = roleRepository.findByName("PAIDUSER")
				.orElseGet(() -> roleRepository.save(new Role("PAIDUSER")));
			Role trialUserRole = roleRepository.findByName("TRIALUSER")
				.orElseGet(() -> roleRepository.save(new Role("TRIALUSER")));
			Role adminRole = roleRepository.findByName("ADMIN")
				.orElseGet(() -> roleRepository.save(new Role("ADMIN")));
		};
	}

	@Bean
	public CommandLineRunner migrateData(
		UserRepository userRepository,
		PlannedExerciseLogRepository plannedExerciseLogRepository,
		WorkoutRepository workoutRepository,
		CompletedWorkoutRepository completedWorkoutRepository,
		ExerciseLogRepository exerciseLogRepository,
		@Value("${migrate.enabled:false}") boolean migrateEnabled) {
		return (args) -> {
			if(migrateEnabled) {
				migrateUsers(userRepository);
				migratePlannedExerciseLogs(plannedExerciseLogRepository);
				migrateWorkouts(workoutRepository);
				migrateCompletedWorkouts(completedWorkoutRepository);
				migrateExerciseLogs(exerciseLogRepository);
				System.out.println("Data migration completed");
			} else {
				System.out.println("Data migration is disabled");
			}
		};
	}

	private void migrateUsers(UserRepository userRepository) {
		List<User> users = userRepository.findAll();
		for (User user : users) {
			boolean updated = false;

			if (user.getUsername() != null) {
				String username = user.getUsername().toLowerCase();
				
				user.setUsername(username + " ");
				System.out.println("Saved dummy username: " + user);
				userRepository.save(user);

				user.setUsername(username);
				System.out.println("Saved real username: " + user);
				userRepository.save(user);

				updated = true;
			}

			if (user.getEmail() != null) {
				String email = user.getEmail().toLowerCase();
				
				user.setEmail(email + " ");
				System.out.println("Saved dummy email: " + user);
				userRepository.save(user);

				user.setEmail(email);
				System.out.println("Saved real email: " + user);
				userRepository.save(user);

				updated = true;
			}

			if (user.getUsernameHash() == null || user.getUsernameHash().isEmpty()) {
				String username = user.getUsername().toLowerCase();
				if (username != null && !username.isEmpty()) {
					String usernameHash = SecurityUtils.hash(username);
					user.setUsernameHash(usernameHash);
					System.out.println("Set usernameHash for User ID " + user.getId());
					updated = true;
				}
			}

			if (user.getEmailHash() == null || user.getEmailHash().isEmpty()) {
				String email = user.getEmail().toLowerCase();
				if (email != null && !email.isEmpty()) {
					String emailHash = SecurityUtils.hash(email);
					user.setEmailHash(emailHash);
					System.out.println("Set emailHash for User ID " + user.getId());
					updated = true;
				}
			}

			if (updated) {
				System.out.println("Migrated User: " + user);
				userRepository.save(user);
			}
		}
	}


	private void migratePlannedExerciseLogs(PlannedExerciseLogRepository plannedExerciseLogRepository) {
		List<PlannedExerciseLog> plannedExerciseLogs = plannedExerciseLogRepository.findAll();
		for (PlannedExerciseLog pel : plannedExerciseLogs) {
			boolean updated = false;

			if (pel.getNotes() != null) {
				String notes = pel.getNotes();

				pel.setNotes(notes + " ");
				System.out.println("Saved dummy PlannedExerciseLog: " + pel);
				plannedExerciseLogRepository.save(pel);

				pel.setNotes(notes);
				System.out.println("Saved real PlannedExerciseLog: " + pel);
				plannedExerciseLogRepository.save(pel);

				updated = true;
			}

			if (updated) {
				System.out.println("Migrated PlannedExerciseLog: " + pel);
			}
		}
	}


	private void migrateWorkouts(WorkoutRepository workoutRepository) {
		List<Workout> workouts = workoutRepository.findAll();
		for (Workout workout : workouts) {
			boolean updated = false;

			if (workout.getName() != null) {
				String name = workout.getName();

				workout.setName(name + " ");
				System.out.println("Saved dummy Workout: " + workout);
				workoutRepository.save(workout);

				workout.setName(name);
				System.out.println("Saved real Workout: " + workout);
				workoutRepository.save(workout);

				updated = true;
			}

			if (workout.getNotes() != null) {
				String notes = workout.getNotes();

				workout.setNotes(notes + " ");
				System.out.println("Saved dummy Workout Notes: " + workout);
				workoutRepository.save(workout);

				workout.setNotes(notes);
				System.out.println("Saved real Workout Notes: " + workout);
				workoutRepository.save(workout);

				updated = true;
			}

			if (updated) {
				System.out.println("Migrated Workout: " + workout);
			}
		}
	}


	private void migrateCompletedWorkouts(CompletedWorkoutRepository completedWorkoutRepository) {
		List<CompletedWorkout> completedWorkouts = completedWorkoutRepository.findAll();
		for (CompletedWorkout cw : completedWorkouts) {
			boolean updated = false;

			if (cw.getWorkoutName() != null) {
				String name = cw.getWorkoutName();

				cw.setWorkoutName(name + " ");
				System.out.println("Saved dummy CompletedWorkout Name: " + cw);
				completedWorkoutRepository.save(cw);

				cw.setWorkoutName(name);
				System.out.println("Saved real CompletedWorkout Name: " + cw);
				completedWorkoutRepository.save(cw);

				updated = true;
			}

			if (cw.getWorkoutNotes() != null) {
				String notes = cw.getWorkoutNotes();

				cw.setWorkoutNotes(notes + " ");
				System.out.println("Saved dummy CompletedWorkout Notes: " + cw);
				completedWorkoutRepository.save(cw);

				cw.setWorkoutNotes(notes);
				System.out.println("Saved real CompletedWorkout Notes: " + cw);
				completedWorkoutRepository.save(cw);

				updated = true;
			}

			if (updated) {
				System.out.println("Migrated CompletedWorkout: " + cw);
			}
		}
	}

	private void migrateExerciseLogs(ExerciseLogRepository exerciseLogRepository) {
		List<ExerciseLog> exerciseLogs = exerciseLogRepository.findAll();
		for (ExerciseLog el : exerciseLogs) {
			if (el.getNotes() != null) {
				String notes = el.getNotes();

				el.setNotes(notes + " ");
				System.out.println("Saved dummy ExerciseLog: " + el);
				exerciseLogRepository.save(el);

				el.setNotes(notes);
				System.out.println("Saved real ExerciseLog: " + el);
				exerciseLogRepository.save(el);
			}
		}
	}

}
