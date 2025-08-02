package mx.edu.uteq.backS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableFeignClients
@SpringBootApplication
public class BackSApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackSApplication.class, args);
	}

}
