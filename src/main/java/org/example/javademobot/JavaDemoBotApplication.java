package org.example.javademobot;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaDemoBotApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("LOG_FILE_PATH", dotenv.get("LOG_FILE_PATH_ENV"));
        System.setProperty("BOT_NAME", dotenv.get("BOT_NAME_ENV"));
        System.setProperty("BOT_TOKEN", dotenv.get("BOT_TOKEN_ENV"));
        SpringApplication.run(JavaDemoBotApplication.class, args);
    }

}
