package jsl.group.microservices.integration.service;

import com.google.protobuf.Int32Value;
import jsl.group.microservices.integration.models.Question;
import jsl.group.question.QuestionOuterClass;
import jsl.group.question.QuestionServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionClient implements QuestionService {
    private static final Logger log = LoggerFactory.getLogger(QuestionClient.class);
    private final QuestionServiceGrpc.QuestionServiceBlockingStub questionServiceBlockingStub;

    public QuestionClient(QuestionServiceGrpc.QuestionServiceBlockingStub questionServiceBlockingStub) {
        this.questionServiceBlockingStub = questionServiceBlockingStub;
    }

    @Override
    public String addQuestion(Question question) {
        QuestionOuterClass.Question clientQuestion = QuestionOuterClass.Question.newBuilder()
                .setId(QuestionOuterClass.QuestionId.newBuilder().setValue(UUID.randomUUID().toString()).build())
                .setAnswer(question.answer())
                .setChoices(question.choices())
                .setQuestion(question.question())
                .build();
        QuestionOuterClass.QuestionId questionId = questionServiceBlockingStub.addQuestion(clientQuestion);
        return questionId.getValue();
    }

    @Override
    public Question getQuestionById(String questionId) {
        QuestionOuterClass.QuestionId clientQuestionId = QuestionOuterClass.QuestionId.newBuilder().setValue(questionId).build();
        QuestionOuterClass.Question question = questionServiceBlockingStub.getQuestionById(clientQuestionId);
        return new Question(questionId, question.getQuestion(), question.getChoices(), question.getAnswer());
    }

    @Override
    public Iterable<Question> getQuestions(int limit) {
        Int32Value int32Value = Int32Value.newBuilder().setValue(limit).build();
        List<Question> questionList = new ArrayList<>(limit);
        questionServiceBlockingStub.getQuestions(int32Value).forEachRemaining(question -> {
            questionList.add(new Question(question.getId().getValue(), question.getQuestion(), question.getChoices(), question.getAnswer()));
        });
        return questionList;
    }
}
