package bybit.bybit_exel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "bybit.bybit_exel")
public class BybitExelApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(BybitExelApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BybitExelApplication.class);
	}
}
