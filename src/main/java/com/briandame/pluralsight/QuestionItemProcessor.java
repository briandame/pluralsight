package com.briandame.pluralsight;

import com.briandame.pluralsight.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

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
        question.setDistractors(source.getDistractors());
        question.setCreatedAt(now);
        question.setUpdatedAt(now);

        question.parseOperator();
        if (question.getStatus().equals(Question.STATUS_ERROR)) {
            LOG.info("Operator error converting (" + source + ") into (" + question + "); status = " + question.getStatus());
            return question;
        }

        question.validateQuestionAndAnswer();
        if (question.getStatus().equals(Question.STATUS_ERROR)) {
            LOG.info("Question and answer error converting (" + source + ") into (" + question + "); status = " + question.getStatus());
            return question;
        }

        question.validateDistractorList();
        if (question.getStatus().equals(Question.STATUS_ERROR)) {
            LOG.info("Distractor error converting (" + source + ") into (" + question + "); status = " + question.getStatus());
            return question;
        }

        LOG.info("Converting (" + source + ") into (" + question + "); status = " + question.getStatus());
        question.setStatus(Question.STATUS_ACTIVE);
        return question;
    }
}
