package it.intesys.orderservice.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URI;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Validated
public record ApplicationProperties(@NotNull String shippingServiceBaseURL, @NotNull String oauth2ShippingServiceRegistrationId) {
}
