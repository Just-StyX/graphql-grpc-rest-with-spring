package jsl.group.microservices.integration.rest;

import jsl.group.microservices.integration.models.*;
import jsl.group.microservices.integration.service.QuestionService;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("question")
public class ClientController {
    private final QuestionService questionService;
    private final HttpGraphQlClient httpGraphQlClient;

    public ClientController(QuestionService questionService, HttpGraphQlClient httpGraphQlClient) {
        this.questionService = questionService;
        this.httpGraphQlClient = httpGraphQlClient;
    }

    @PostMapping
    public Mono<String> addQuestion(@RequestBody Question question) {
        return Mono.just(questionService.addQuestion(question));
    }

    @GetMapping("/{question-id}")
    public Mono<ClientQuestion> clientQuestionMono(@PathVariable(value = "question-id") String questionId) {
        String document = """
                query question($questionId: String) {
                  question(questionId: $questionId) {
                    questionId
                    question
                    choices
                  }
                }""";
        Mono<Question> responseEntity = httpGraphQlClient.document(document).variable("questionId", questionId).retrieve("question").toEntity(Question.class);
        return responseEntity.map(this::questionToClientQuestion);
    }

    @GetMapping
    public Mono<List<ClientQuestion>> clientQuestionFlux(@RequestParam(value = "limit", defaultValue = "5") int limit) {
        String document = """
                query questions($limit: Int) {
                  questions(limit: $limit) {
                    questionId
                    question
                    choices
                  }
                }""";
        Mono<List<Question>> responseEntity = httpGraphQlClient.document(document).variable("limit", limit).retrieve("questions").toEntityList(Question.class);
        return responseEntity.map(list -> list.stream().map(this::questionToClientQuestion).toList());
    }

    @MutationMapping
    public Mono<Question> createQuestion(@Argument InputQuestion inputQuestion) {
        Question question = new Question(inputQuestion.questionId(), inputQuestion.question(), inputQuestion.choices(), inputQuestion.answer());
        String questionId = questionService.addQuestion(question);
        return Mono.just(new Question(questionId, inputQuestion.question(), inputQuestion.choices(), inputQuestion.answer()));
    }

    @SchemaMapping
    Flux<ClientQuestion> pages(ScrollSubrange scrollSubrange) {
        MutableSortDefinition mutableSortDefinition = new MutableSortDefinition();
        mutableSortDefinition.setProperty("question");
        mutableSortDefinition.setAscending(true);
        List<Question> questionList = (List<Question>) questionService.getQuestions(5);
        PagedListHolder<Question> pagedListHolder = new PagedListHolder<>(questionList, mutableSortDefinition);
        pagedListHolder.setPageSize(1);
        pagedListHolder.setPage((int) scrollSubrange.position().stream().count());
        List<ClientQuestion> clientQuestion = pagedListHolder.getPageList().stream().map(this::questionToClientQuestion).toList();
        return Flux.fromIterable(clientQuestion);
    }

    @QueryMapping
    Mono<Question> question(@Argument String questionId) {
        return Mono.just(questionService.getQuestionById(questionId));
    }

    @QueryMapping
    Flux<Question> questions(@Argument int limit) {
        return Flux.fromIterable(questionService.getQuestions(limit));
    }

    private ClientQuestion questionToClientQuestion(Question question) {
        if (question.choices() != null) {
            Map<Character, String> choices = new HashMap<>();
            Arrays.stream(question.choices().split("; ")).forEach(string -> {
                String[] arrayString = string.split("#");
                choices.put(arrayString[0].charAt(0), arrayString[1]);
            });
            return new ClientQuestion(question.questionId(), question.question(), choices);
        } else {
            return new ClientQuestion(question.questionId(), question.question(), Map.of());
        }
    }
}
