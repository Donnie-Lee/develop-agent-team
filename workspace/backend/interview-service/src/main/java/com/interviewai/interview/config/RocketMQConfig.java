package com.interviewai.interview.config;

import lombok.Data;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.rocketmq")
public class RocketMQConfig {

    private String namesrvAddr;
    private ProducerConfig producer = new ProducerConfig();

    @Data
    public static class ProducerConfig {
        private String group;
    }

    @Bean
    public DefaultMQProducer defaultMQProducer() {
        DefaultMQProducer mqProducer = new DefaultMQProducer();
        mqProducer.setNamesrvAddr(namesrvAddr);
        mqProducer.setProducerGroup(producer.getGroup());
        return mqProducer;
    }
}
