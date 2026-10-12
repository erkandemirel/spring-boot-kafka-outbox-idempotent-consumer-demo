package com.example.reliable.order;

import com.example.reliable.messages.EventTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class KafkaTopicConfiguration {
    @Bean
    NewTopic orderCreatedTopic() {
        return TopicBuilder.name(EventTopics.ORDER_CREATED).partitions(3).replicas(1).build();
    }
}
