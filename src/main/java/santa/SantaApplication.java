package santa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SantaConfig.class)
public class SantaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SantaApplication.class, args);
    }
}
