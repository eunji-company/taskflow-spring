package indiv.abko.taskflow;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TaskflowApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskflowApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(StartUpService startUpService) {
		return args -> {
			startUpService.runOnStartup();
		};
	}
}
