package lk.campuslk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CampuslkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampuslkApiApplication.class, args);
	}

}
