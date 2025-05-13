package jsl.group.microservices.integration.models;

import java.util.Map;

public record ClientQuestion(
        String questionId, String question, Map<Character, String> choices
) {
}
