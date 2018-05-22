package com.briandame.pluralsight.repository;

import com.briandame.pluralsight.model.Question;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * QuestionRepository -
 *
 * @author briandame@gmail.com
 */
public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
}
