package net.troja.demo.gelfloggenerator;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GeneratorApplication {
    private static final Logger LOGGER = LogManager.getLogger(GeneratorApplication.class);

    @Autowired
    private HealthLogger health;

    public GeneratorApplication() {
        LOGGER.info("Starting up...");
    }

    @PostConstruct
    public void run() {

    }

    public static void main(String[] args) {
        SpringApplication.run(GeneratorApplication.class, args);
    }
}
