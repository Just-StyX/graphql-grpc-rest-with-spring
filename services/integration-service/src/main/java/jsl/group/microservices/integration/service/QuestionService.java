package jsl.group.microservices.integration.service;

import jsl.group.microservices.integration.models.Question;

public interface QuestionService {
    String addQuestion(Question question);
    Question getQuestionById(String questionId);
    Iterable<Question> getQuestions(int limit);
}
