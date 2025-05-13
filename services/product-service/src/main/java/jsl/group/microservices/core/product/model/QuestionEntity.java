package jsl.group.microservices.core.product.model;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Version
    private Integer version;
    @Column(nullable = false)
    private String question;
    private String choices;
    @Column(nullable = false)
    private String answer;

    public QuestionEntity() {}

    public QuestionEntity(String question, String choices, String answer) {
        this.question = question;
        this.choices = choices;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "QuestionEntity{" +
                "productId='" + id + '\'' +
                ", version=" + version +
                ", question='" + question + '\'' +
                ", choices='" + choices + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
