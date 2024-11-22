package it.intesys.shippingservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.intesys.shippingservice.client.api.DefaultApi;
import it.intesys.shippingservice.client.invoker.ApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Configuration
public class RestClientConfiguration {

    @Bean
    public DefaultApi getShippingProviderRestClientApi(ApplicationProperties applicationProperties,
                    LogbookClientHttpRequestInterceptor logbookClientHttpRequestInterceptor, ObjectMapper objectMapper) {

        RestClient restClient = ApiClient.buildRestClientBuilder(objectMapper)
                        .baseUrl(applicationProperties.shippingProviderBaseURL())
                        .build()
                        .mutate()
                        .requestInterceptor(logbookClientHttpRequestInterceptor)
                        .build();
        return new DefaultApi(new ApiClient(restClient));
    }

}
