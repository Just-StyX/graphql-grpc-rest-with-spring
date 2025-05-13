package jsl.group.microservices.integration.models;

public record Question(
        String questionId, String question, String choices, String answer
) {
}
