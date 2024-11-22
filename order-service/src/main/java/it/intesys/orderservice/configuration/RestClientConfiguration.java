package it.intesys.orderservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.intesys.orderservice.client.api.DefaultApi;
import it.intesys.orderservice.client.invoker.ApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Configuration
public class RestClientConfiguration {

    @Bean
    public DefaultApi getShippingRestClientApi(ApplicationProperties applicationProperties, AuthorizedClientServiceOAuth2AuthorizedClientManager manager,
                    LogbookClientHttpRequestInterceptor logbookClientHttpRequestInterceptor, ObjectMapper objectMapper) {

        RestClient restClient = ApiClient.buildRestClientBuilder(objectMapper)
                        .baseUrl(applicationProperties.shippingServiceBaseURL())
                        .build()
                        .mutate()
                        .requestInterceptor(
                                        new ClientCredentialsOauth2AccessTokenInterceptor(manager, applicationProperties.oauth2ShippingServiceRegistrationId()))
                        .requestInterceptor(logbookClientHttpRequestInterceptor)
                        .build();
        return new DefaultApi(new ApiClient(restClient));
    }
}
