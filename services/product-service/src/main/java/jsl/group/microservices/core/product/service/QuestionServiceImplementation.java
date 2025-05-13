package jsl.group.microservices.core.product.service;

import com.google.protobuf.Int32Value;
import io.grpc.stub.StreamObserver;
import jsl.group.microservices.core.product.model.QuestionEntity;
import jsl.group.microservices.core.product.model.QuestionEntityRepository;
import jsl.group.question.QuestionOuterClass;
import jsl.group.question.QuestionServiceGrpc;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QuestionServiceImplementation extends QuestionServiceGrpc.QuestionServiceImplBase {
    private final QuestionEntityRepository questionEntityRepository;

    public QuestionServiceImplementation(QuestionEntityRepository questionEntityRepository) {
        this.questionEntityRepository = questionEntityRepository;
    }

    @Override
    public void addQuestion(QuestionOuterClass.Question question, StreamObserver<QuestionOuterClass.QuestionId> questionIdStreamObserver) {
        String theQuestion = question.getQuestion();
        String choices = question.getChoices();
        String answer = question.getAnswer();
        QuestionEntity questionEntity = new QuestionEntity(theQuestion, choices, answer);
        QuestionEntity savedQuestion = questionEntityRepository.save(questionEntity);
        QuestionOuterClass.QuestionId questionId = QuestionOuterClass.QuestionId.newBuilder().setValue(savedQuestion.getId()).build();
        questionIdStreamObserver.onNext(questionId);
        questionIdStreamObserver.onCompleted();
    }

    @Override
    public void getQuestionById(QuestionOuterClass.QuestionId questionId, StreamObserver<QuestionOuterClass.Question> questionStreamObserver) {
        String id = questionId.getValue();
        QuestionEntity questionEntity = questionEntityRepository.findById(id).get();
        QuestionOuterClass.Question foundQuestion = QuestionOuterClass.Question.newBuilder()
                .setId(questionId)
                .setQuestion(questionEntity.getQuestion())
                .setChoices(questionEntity.getChoices())
                .setAnswer(questionEntity.getAnswer())
                .build();
        questionStreamObserver.onNext(foundQuestion);
        questionStreamObserver.onCompleted();
    }

    @Override
    public void getQuestions(Int32Value numberOfQuestions, StreamObserver<QuestionOuterClass.Question> questionStreamObserver) {
        List<QuestionEntity> questionEntities = questionEntityRepository.findAll();
        Collections.shuffle(questionEntities);
        List<QuestionEntity> questionEntityList = numberOfQuestions.getValue() > questionEntities.size() ? questionEntities : questionEntities.subList(0, numberOfQuestions.getValue());
        for (QuestionEntity questionEntity: questionEntityList) {
            QuestionOuterClass.Question question = QuestionOuterClass.Question.newBuilder()
                    .setId(QuestionOuterClass.QuestionId.newBuilder().setValue(questionEntity.getId()).build())
                    .setQuestion(questionEntity.getQuestion())
                    .setChoices(questionEntity.getChoices())
                    .setAnswer(questionEntity.getAnswer())
                    .build();
            questionStreamObserver.onNext(question);
        }
        questionStreamObserver.onCompleted();
    }
}
