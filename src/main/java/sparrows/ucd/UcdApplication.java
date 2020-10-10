package sparrows.ucd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import sparrows.ucd.services.TestService;

@SpringBootApplication
public class UcdApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(UcdApplication.class, args);
		System.out.println("now I'm here");

		//ApplicationContext applicationContext = SpringApplication.run(UcdApplication.class, args);
		TestService service = context.getBean(TestService.class);
		service.Insert();

		System.out.println("now I'm there");

		SpringApplication.exit(context, () -> 0);
	}

}
