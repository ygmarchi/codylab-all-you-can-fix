package it.intesys.orderservice.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SpringDocConfiguration {

    private final Optional<GitProperties> gitProperties;

    @Bean
    public OpenAPI openapi() {

        return new OpenAPI()
            .info(new Info().title("Order Service").version(gitProperties.map(GitProperties::getShortCommitId).orElse(null)));
    }
}
