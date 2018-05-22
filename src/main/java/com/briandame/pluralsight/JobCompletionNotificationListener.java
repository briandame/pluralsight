package com.briandame.pluralsight;

import com.briandame.pluralsight.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * JobCompletionNotificationListener -
 *
 * @author briandame@gmail.com
 */
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger LOG = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOG.info("!!! JOB FINISHED! Time to verify the results");

            jdbcTemplate.query("SELECT question, answer, distractors FROM question",
                    (rs, row) -> new Question(
                            rs.getString(1),
                            rs.getInt(2),
                            rs.getString(3))
            ).forEach(question -> LOG.info("Found <" + question + "> in the database."));
        }
    }
}
