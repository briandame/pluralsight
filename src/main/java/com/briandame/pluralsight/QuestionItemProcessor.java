package com.briandame.pluralsight;

import com.briandame.pluralsight.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * QuestionItemProcessor -
 *
 * @author briandame@gmail.com
 */
public class QuestionItemProcessor implements ItemProcessor<Question,Question> {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionItemProcessor.class);

    @Override
    public Question process(final Question source) {

        LocalDateTime now = LocalDateTime.now();

        Question question = new Question();
        question.setQuestion(source.getQuestion());
        question.setAnswer(source.getAnswer());
        question.setDistrators(source.getDistractors());
        question.setCreatedAt(now);
        question.setUpdatedAt(now);

        String operator = parseOperator(source);
        if (operator == null) {
            question.setStatus(Question.STATUS_ERROR);
            LOG.info("Converting (" + source + ") into (" + question + ")");
            return question;
        }
        question.setOperator(operator);

        if (!isQuestionAndAnswerValid(source, operator)) {
            question.setStatus(Question.STATUS_ERROR);
            LOG.info("Converting (" + source + ") into (" + question + ")");
            return question;
        }

        if (!isDistractorListValid(source)) {
            question.setStatus(Question.STATUS_ERROR);
            LOG.info("Converting (" + source + ") into (" + question + ")");
            return question;
        }


        question.setStatus(Question.STATUS_ACTIVE);
        return question;
    }

    /**
     * Validates the assumption that a question has a valid operator and that
     * it represents addition, subtraction, multiplication or division.
     *
     * @param source
     * @return a String value representing a valid operator or null if a valid operator is not found.
     */
    private String parseOperator(Question source) {
        // Assuming that a question contains only one operator. This function can
        // be improved by validating that assumption.
        String operator = null;
        if (source.getQuestion().contains(Question.OPERATOR_ADD)) {
            operator = Question.OPERATOR_ADD;
        } else if (source.getQuestion().contains(Question.OPERATOR_SUBTRACT)) {
            operator = Question.OPERATOR_SUBTRACT;
        } else if (source.getQuestion().contains(Question.OPERATOR_MULTIPLY)) {
            operator = Question.OPERATOR_MULTIPLY;
        } else if (source.getQuestion().contains(Question.OPERATOR_DIVIDE)) {
            operator = Question.OPERATOR_DIVIDE;
        }
        return operator;
    }

    /**
     * Validates the assumption that there are only two integers in the question
     * and that performing the operation results in the provided answer.
     *
     * @param source
     * @param operator
     * @return a boolean value
     */
    private boolean isQuestionAndAnswerValid(Question source, String operator) {
        Pattern pattern = Pattern.compile("-?\\d+");
        Matcher matcher = pattern.matcher(source.getQuestion());
        List<Integer> values = new ArrayList<>();
        while (matcher.find()) {
            Integer value = Integer.parseInt(matcher.group());
            values.add(value);
        }

        if (values.size() != 2) {
            return false;
        } else {
            boolean isValid = true;
            Integer value1 = values.get(0);
            Integer value2 = values.get(1);
            switch (operator) {
                case Question.OPERATOR_ADD:
                    isValid = value1 + value2 == source.getAnswer();
                    break;
                case Question.OPERATOR_SUBTRACT:
                    isValid = value1 - value2 == source.getAnswer();
                    break;
                case Question.OPERATOR_MULTIPLY:
                    isValid = value1 * value2 == source.getAnswer();
                    break;
                case Question.OPERATOR_DIVIDE:
                    isValid = value1 / value2 == source.getAnswer();
                    break;
            }
            return isValid;
        }
    }

    /**
     * Validates that none of the distractors is the same value as the answer.
     *
     * @param source
     * @return a boolean value
     */
    private boolean isDistractorListValid(Question source) {
        String[] array = source.getDistractors().replace(" ", "").split(",");
        for (String value : array) {
            try {
                Integer intDistractor = Integer.parseInt(value);
                if (intDistractor.equals(source.getAnswer())) {
                    return false;
                }
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
                return false;
            }
        }
        return true;
    }
}
