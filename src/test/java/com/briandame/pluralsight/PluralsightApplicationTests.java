package com.briandame.pluralsight;

import com.briandame.pluralsight.model.Question;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PluralsightApplicationTests {

	private Question source;

	@InjectMocks
	private QuestionItemProcessor processor;

	@Before
	public void setUp() {
        source = new Question();
        source.setQuestion("What is 2 + 2?");
        source.setAnswer(4);
        source.setDistrators("5,6,7,8");
	}

	@Test
	public void testInvalidOperator() {
        source.setQuestion("What is 2 # 3?");
		Question question = processor.process(source);
		assertThat(question.getStatus(), is(Question.STATUS_ERROR));
	}

    @Test
    public void testValidOperator() {
        Question question = processor.process(source);
        assertThat(question.getStatus(), is(Question.STATUS_ACTIVE));
    }

    @Test
    public void testMoreThanTwoIntegersInQuestion() {
        source.setQuestion("What is 2 + 3 + 4?");
        Question question = processor.process(source);
        assertThat(question.getStatus(), is(Question.STATUS_ERROR));
    }

    @Test
    public void testInvalidAdditionAnswer() {
        source.setAnswer(5);
        Question question = processor.process(source);
        assertThat(question.getStatus(), is(Question.STATUS_ERROR));
    }

    @Test
    public void testInvalidSubtractionAnswer() {
	    source.setQuestion("What is 5 - 3?");
        source.setAnswer(1);
        Question question = processor.process(source);
        assertThat(question.getStatus(), is(Question.STATUS_ERROR));
    }

    @Test
    public void testValidSubtractionAnswer() {
        source.setQuestion("What is 5 - 3?");
        source.setAnswer(2);
        Question question = processor.process(source);
        assertThat(question.getStatus(), is(Question.STATUS_ACTIVE));
    }

    @Test
    public void testInvalidMultiplicationAnswer() {
        source.setQuestion("What is 2 * 2?");
        source.setAnswer(1);
        Question question = processor.process(source);
        assertThat(question.getStatus(), is(Question.STATUS_ERROR));
    }

    @Test
    public void testValidMultiplicationAnswer() {
        source.setQuestion("What is 2 * 2?");
        Question question = processor.process(source);
        assertThat(question.getStatus(), is(Question.STATUS_ACTIVE));
    }

    @Test
    public void testInvalidDivisionAnswer() {
        source.setQuestion("What is 10 / 2?");
        source.setAnswer(1);
        Question question = processor.process(source);
        assertThat(question.getStatus(), is(Question.STATUS_ERROR));
    }

    @Test
    public void testValidDivisionAnswer() {
        source.setQuestion("What is 8 / 2?");
        Question question = processor.process(source);
        assertThat(question.getStatus(), is(Question.STATUS_ACTIVE));
    }

    @Test
    public void testInvalidDistractors() {
        source.setDistrators("1,2,3,4");
        Question question = processor.process(source);
        assertThat(question.getStatus(), is(Question.STATUS_ERROR));
    }

}
