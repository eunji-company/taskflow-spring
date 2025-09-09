package indiv.abko.taskflow.global.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import indiv.abko.taskflow.StartUpService;

@Configuration
public class StartUpConfig {
    @Bean
    public CommandLineRunner init(StartUpService startUpService) {
        return args -> {
            startUpService.runOnStartup();
        };
    }
}
