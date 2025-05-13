package jsl.group.microservices.integration.models;

public record PageInfo(
        String startCursor, String endCursor, Boolean hasNextPage, Boolean hasPreviousPage
) {
}
