package jsl.group.microservices.integration.config;

import jsl.group.question.QuestionServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientChannelConfiguration {
    @Bean
    public QuestionServiceGrpc.QuestionServiceBlockingStub questionServiceBlockingStub(GrpcChannelFactory channelFactory) {
        return QuestionServiceGrpc.newBlockingStub(channelFactory.createChannel("question"));
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public HttpGraphQlClient graphQlClient() {
        return HttpGraphQlClient.builder(webClient()).url("http://integration-service/data").build();
    }
}
