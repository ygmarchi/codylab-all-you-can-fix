package it.intesys.orderservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic orderCreatedTopic() {
        return new NewTopic("order.created", 1, (short) 1);
    }

    @Bean
    public NewTopic orderShippingTopic() {
        return new NewTopic("order.shipping", 1, (short) 1);
    }

    @Bean
    public NewTopic orderShippedTopic() {
        return new NewTopic("order.shipped", 1, (short) 1);
    }
}
