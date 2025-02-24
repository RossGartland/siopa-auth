package com.siopa.siopa_auth.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Kafka consumer settings.
 * Defines the consumer factory and Kafka listener container factory.
 */
@Configuration
public class KafkaConfig {

    /**
     * Creates and configures the Kafka consumer factory.
     * This factory is responsible for creating Kafka consumers that process messages as Strings.
     *
     * @return A configured Kafka {@link ConsumerFactory} for String key-value pairs.
     */
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // ✅ Use StringDeserializer

        return new DefaultKafkaConsumerFactory<>(config);
    }

    /**
     * Creates and configures the Kafka listener container factory.
     * This factory is used by Kafka listeners to consume messages from topics.
     *
     * @return A configured {@link ConcurrentKafkaListenerContainerFactory} for String messages.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
