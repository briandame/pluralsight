package com.briandame.pluralsight.controller;

import com.briandame.pluralsight.model.Question;
import com.briandame.pluralsight.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * QuestionsController -
 *
 * @author briandame@gmail.com
 */
@RestController
@RequestMapping(path = "/questions")
@CrossOrigin
public class QuestionsController {

    @Autowired
    private QuestionRepository repository;

    @GetMapping
    @ResponseBody
    public HttpEntity<?> list(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size) {

        Sort sort = Sort.by(Sort.Order.desc("id"));
        Page<Question> questions = repository.findAll(PageRequest.of(page, size, sort));
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping(path="/{id}")
    @ResponseBody
    public HttpEntity<?> findById(@PathVariable("id") Long id) {

        Optional<Question> question = repository.findById(id);
        if (!question.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public HttpEntity<?> create(@RequestBody Question question) {

        LocalDateTime now = LocalDateTime.now();
        question.setStatus(Question.STATUS_ACTIVE);
        question.setOperator(question.parseOperator());
        question.setCreatedAt(now);
        question.setUpdatedAt(now);
        repository.save(question);
        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    @ResponseBody
    public HttpEntity<?> update(
            @PathVariable("id") final Long id,
            @RequestBody Question question) {

        Optional<Question> questionOptional = repository.findById(id);
        if (!questionOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Question updatedQuestion = questionOptional.get();

        LocalDateTime now = LocalDateTime.now();
        updatedQuestion.setQuestion(question.getQuestion());
        updatedQuestion.setAnswer(question.getAnswer());
        updatedQuestion.setDistractorsList(question.getDistractorsList());
        updatedQuestion.setUpdatedAt(now);

        repository.save(question);
        return new ResponseEntity<>(question, HttpStatus.OK);
    }
}
