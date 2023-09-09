package com.ff.intg.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "rabbitmq", ignoreUnknownFields = false)
public class RabbitmqConn {

    private String uri;

    @Bean
    public CachingConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory cf = new CachingConnectionFactory();
        cf.setUri(this.uri);
        return cf;
    }

    public void setUri(String uri) {
        // this method is needed for autoconfig
        this.uri = uri;
    }
}
