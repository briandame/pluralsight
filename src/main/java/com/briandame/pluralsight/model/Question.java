package com.briandame.pluralsight.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Question -
 *
 * @author briandame@gmail.com
 */
@Entity
@JsonPropertyOrder({ "id", "question", "answer", "distractors", "createdAt", "updatedAt" })
public class Question {

    private static final Logger LOG = LoggerFactory.getLogger(Question.class);

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_NOT_ACTIVE = 2;
    public static final Integer STATUS_ERROR = 3;
    public static final Integer STATUS_DELETED = 4;

    public static final String OPERATOR_ADD = "+";
    public static final String OPERATOR_SUBTRACT = "-";
    public static final String OPERATOR_MULTIPLY = "*";
    public static final String OPERATOR_DIVIDE = "/";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private Integer answer;

    @Column(name = "distractors")
    @JsonIgnore
    private String distrators;

    @Column(name = "operator")
    @JsonIgnore
    private String operator;

    @Column(name = "status")
    @JsonIgnore
    private Integer status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Question() {
    }

    public Question(String question, Integer answer, String distractors) {
        this.question = question;
        this.answer = answer;
        this.distrators = distractors;
    }

    public String parseOperator() {
        String operator = null;
        if (this.getQuestion().contains(Question.OPERATOR_ADD)) {
            operator = Question.OPERATOR_ADD;
        } else if (this.getQuestion().contains(Question.OPERATOR_SUBTRACT)) {
            operator = Question.OPERATOR_SUBTRACT;
        } else if (this.getQuestion().contains(Question.OPERATOR_MULTIPLY)) {
            operator = Question.OPERATOR_MULTIPLY;
        } else if (this.getQuestion().contains(Question.OPERATOR_DIVIDE)) {
            operator = Question.OPERATOR_DIVIDE;
        } else {
            status = Question.STATUS_ERROR;
        }
        return operator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    public String getDistractors() {
        return distrators;
    }

    @JsonProperty("distractors")
    public List<Integer> getDistractorsList() {
        List<Integer> list = new ArrayList<>();
        String[] array = distrators.replace(" ", "").split(",");
        for (String value : array) {
            try {
                Integer intDistractor = Integer.parseInt(value);
                list.add(intDistractor);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
        return list;
    }

    public void setDistrators(String distrators) {
        this.distrators = distrators;
    }

    @JsonProperty("distractors")
    public void setDistractorsList(List<Integer> list) {
        List<String> strings = list.stream().map(Object::toString)
                .collect(Collectors.toList());
        distrators = String.join(",", strings);
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answer=" + answer +
                ", distrators=" + distrators +
                ", operator='" + operator + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return Objects.equals(id, question1.id) &&
                Objects.equals(question, question1.question) &&
                Objects.equals(answer, question1.answer) &&
                Objects.equals(distrators, question1.distrators) &&
                Objects.equals(operator, question1.operator) &&
                Objects.equals(status, question1.status) &&
                Objects.equals(createdAt, question1.createdAt) &&
                Objects.equals(updatedAt, question1.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, answer, distrators, operator, status, createdAt, updatedAt);
    }
}
