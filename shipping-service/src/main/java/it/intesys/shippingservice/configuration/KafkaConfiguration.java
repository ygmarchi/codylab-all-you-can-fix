package it.intesys.shippingservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic orderCreatedTopic() {
        return new NewTopic("order.shipping", 1, (short) 1);
    }
}
