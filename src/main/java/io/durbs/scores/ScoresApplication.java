package io.durbs.scores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ScoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScoresApplication.class, args);
	}

}
