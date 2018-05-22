package com.briandame.pluralsight;

import com.briandame.pluralsight.model.Question;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

/**
 * BatchConfiguration -
 *
 * @author briandame@gmail.com
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<Question> reader() {
        return new FlatFileItemReaderBuilder<Question>()
                .name("questionItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .lineTokenizer(new DelimitedLineTokenizer() {{
                    setNames("question", "answer", "distractors");
                    setDelimiter("|");
                }})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Question>() {{
                    setTargetType(Question.class);
                }})
                .build();
    }

    @Bean
    public QuestionItemProcessor processor() {
        return new QuestionItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Question> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Question>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO question (question, answer, distractors, operator, status, created_at, updated_at) VALUES (:question, :answer, :distractors, :operator, :status, :createdAt, :updatedAt)")
                .dataSource(dataSource)
                .build();
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importQuestionJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Question> writer) {
        return stepBuilderFactory.get("step1")
                .<Question, Question> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
    // end::jobstep[]
}
