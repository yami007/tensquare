package tensquare.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import utils.JwtUtil;

@SpringBootApplication
@EnableZuulProxy
public class ManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagerApplication.class, args);
	}
	@Bean
	public JwtUtil jwtUtil(){
		return new utils.JwtUtil();
	}
}
