package de.hs.bochum;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoftwareApiApplication implements CommandLineRunner  {

	public static void main(String[] args) {
		SpringApplication.run(SoftwareApiApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		
	}
}
