package org.example.javademobot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
public class BotConfig {
    @Value("${BOT_NAME}")
    String botName;
    @Value("${BOT_TOKEN}")
    String token;
}
