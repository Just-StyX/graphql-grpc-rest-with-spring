package jsl.group.microservices.integration.models;

import reactor.core.publisher.Flux;

public record QuestionConnection(
        Flux<QuestionEdge> edges, PageInfo pageInfo
) {
}
