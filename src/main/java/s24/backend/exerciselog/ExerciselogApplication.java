package s24.backend.exerciselog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.cdimascio.dotenv.Dotenv;
import s24.backend.exerciselog.domain.entity.Role;
import s24.backend.exerciselog.repository.*;

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
}
