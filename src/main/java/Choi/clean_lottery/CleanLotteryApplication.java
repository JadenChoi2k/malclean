package Choi.clean_lottery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CleanLotteryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleanLotteryApplication.class, args);
	}

//	@Bean
//	public AuditorAware<String> modifierAware() {
//
//	}
}
