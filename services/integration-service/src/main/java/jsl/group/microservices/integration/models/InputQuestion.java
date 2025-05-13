package jsl.group.microservices.integration.models;

public record InputQuestion(
        String questionId, String question, String choices, String answer
) {
}
