package ru.zhendozzz.vkbot.config;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public TransportClient httpTransportClient(){
        return new HttpTransportClient();
    }
}
