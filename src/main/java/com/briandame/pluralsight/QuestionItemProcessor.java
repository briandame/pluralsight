package com.briandame.pluralsight;

import com.briandame.pluralsight.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.Instant;

/**
 * QuestionItemProcessor -
 *
 * @author briandame@gmail.com
 */
public class QuestionItemProcessor implements ItemProcessor<Question,Question> {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionItemProcessor.class);

    @Override
    public Question process(final Question source) {

        Integer status = Question.STATUS_ACTIVE;
        String operator = source.parseOperator();

        Instant now = Instant.now();

        Question question = new Question();
        question.setQuestion(source.getQuestion());
        question.setAnswer(source.getAnswer());
        question.setDistrators(source.getDistractors());
        question.setOperator(operator);
        question.setStatus(status);
        question.setCreatedAt(now);
        question.setUpdatedAt(now);

        LOG.info("Converting (" + source + ") into (" + question + ")");

        return question;
    }
}
