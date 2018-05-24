package com.briandame.pluralsight.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String distractors;

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
        this.distractors = distractors;
    }

    /**
     * Validates the assumption that a question has a valid operator and that
     * it represents addition, subtraction, multiplication or division.
     *
     *
     * @return a String value representing a valid operator or null if a valid operator is not found.
     */
    public void parseOperator() {
        status = STATUS_ACTIVE;
        if (this.getQuestion().contains(OPERATOR_ADD)) {
            operator = OPERATOR_ADD;
        } else if (this.getQuestion().contains(OPERATOR_SUBTRACT)) {
            operator = OPERATOR_SUBTRACT;
        } else if (this.getQuestion().contains(OPERATOR_MULTIPLY)) {
            operator = OPERATOR_MULTIPLY;
        } else if (this.getQuestion().contains(OPERATOR_DIVIDE)) {
            operator = OPERATOR_DIVIDE;
        } else {
            status = STATUS_ERROR;
        }
    }

    /**
     * Validates the assumption that there are only two integers in the question
     * and that performing the operation results in the provided answer.
     */
    public void validateQuestionAndAnswer() {
        if (status.equals(STATUS_ERROR)) {
            return;
        }

        if (question == null || answer == null) {
            status = STATUS_ERROR;
            return;
        }

        Pattern pattern = Pattern.compile("-?\\d+");
        Matcher matcher = pattern.matcher(question);
        List<Integer> values = new ArrayList<>();
        while (matcher.find()) {
            Integer value = Integer.parseInt(matcher.group());
            values.add(value);
        }

        if (values.size() != 2) {
            LOG.error("Expected values size 2 (actual " + values.size() + ")");
            status = STATUS_ERROR;
        } else {
            boolean isValid = true;
            Integer value1 = values.get(0);
            Integer value2 = values.get(1);
            switch (operator) {
                case Question.OPERATOR_ADD:
                    Integer addResult = value1 + value2;
                    isValid = addResult.equals(answer);
                    if (!isValid) {
                        LOG.error("Add error: " + value1 + " + " + value2 + " = " + answer);
                    }
                    break;
                case Question.OPERATOR_SUBTRACT:
                    Integer subtractResult = value1 - value2;
                    isValid = subtractResult.equals(answer);
                    if (!isValid) {
                        LOG.error("Subtract error: " + value1 + " - " + value2 + " = " + answer);
                    }
                    break;
                case Question.OPERATOR_MULTIPLY:
                    Integer multiplyResult = value1 * value2;
                    isValid = multiplyResult.equals(answer);
                    if (!isValid) {
                        LOG.error("Multiply error: " + value1 + " * " + value2 + " = " + answer);
                    }
                    break;
                case Question.OPERATOR_DIVIDE:
                    Integer divideResult = value1 / value2;
                    isValid = divideResult.equals(answer);
                    if (!isValid) {
                        LOG.error("Divide error: " + value1 + " / " + value2 + " = " + answer);
                    }
                    break;
            }

            if (!isValid) {
                status = STATUS_ERROR;
            }
        }
    }

    /**
     * Validates that none of the distractors is the same value as the answer.
     *
     * @return a boolean value
     */
    public void validateDistractorList() {
        String[] array = distractors.replace(" ", "").split(",");
        for (String value : array) {
            try {
                Integer intDistractor = Integer.parseInt(value);
                if (intDistractor.equals(answer)) {
                    status = STATUS_ERROR;
                    return;
                }
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
                status = STATUS_ERROR;
                return;
            }
        }
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
        return distractors;
    }

    public void setDistractors(String distractors) {
        this.distractors = distractors;
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
                ", distractors=" + distractors +
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
        return Objects.equals(id, question1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
