package net.troja.demo.gelfloggenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GeneratorApplication {
    private static final Logger LOGGER = LogManager.getLogger(GeneratorApplication.class);

    public GeneratorApplication() {
        LOGGER.info("Starting up...");
    }

    public static void main(String[] args) {
        SpringApplication.run(GeneratorApplication.class, args);
    }
}
