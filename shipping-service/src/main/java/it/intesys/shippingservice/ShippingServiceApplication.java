package it.intesys.shippingservice;

import it.intesys.shippingservice.configuration.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class ShippingServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(ShippingServiceApplication.class, args);
    }

}
