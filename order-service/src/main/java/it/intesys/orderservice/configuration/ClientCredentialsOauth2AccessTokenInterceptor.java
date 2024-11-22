package it.intesys.orderservice.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class ClientCredentialsOauth2AccessTokenInterceptor implements ClientHttpRequestInterceptor {

    private final AuthorizedClientServiceOAuth2AuthorizedClientManager manager;
    private final String oauth2ClientRegistrationId;

    public ClientCredentialsOauth2AccessTokenInterceptor(AuthorizedClientServiceOAuth2AuthorizedClientManager manager, String oauth2ClientRegistrationId) {

        this.manager = manager;
        this.oauth2ClientRegistrationId = oauth2ClientRegistrationId;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                    throws IOException {

        var req = OAuth2AuthorizeRequest.withClientRegistrationId(oauth2ClientRegistrationId)
                        .principal(oauth2ClientRegistrationId)
                        .build();

        OAuth2AuthorizedClient authorize = manager.authorize(req);
        request.getHeaders()
                        .set(HttpHeaders.AUTHORIZATION, "Bearer " + authorize.getAccessToken()
                                        .getTokenValue());
        return execution.execute(request, body);
    }
}
