package jpabook.jpashop;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.*;


@EnableJpaAuditing
@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) throws IOException {

		SpringApplication.run(JpashopApplication.class, args);

	}

}
