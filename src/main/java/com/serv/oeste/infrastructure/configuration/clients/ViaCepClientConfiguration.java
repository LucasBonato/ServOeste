package com.serv.oeste.infrastructure.configuration.clients;

import com.serv.oeste.application.contracts.clients.ViaCepClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ViaCepClientConfiguration {

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    ViaCepClient viaCepClient(RestClient.Builder builder) {
        RestClient restClient = builder
                .baseUrl("https://viacep.com.br")
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(ViaCepClient.class);
    }
}
