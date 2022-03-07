package ru.zhendozzz.vkbot.config;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = {
    "https://localhost",
    "https://192.168.208.1",
}, allowCredentials = "true")
public interface CorsConfig {
}