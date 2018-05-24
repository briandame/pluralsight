# Pluralsight Coding Challenge
Spring Boot application that implements the Pluralsight coding challenge

https://dame-pluralsight.herokuapp.com/questions

This application provides a RESTful web service for managing questions, answers and distractors. The data from the source CSV file is imported on application start using Spring Batch and inserted into a PostgreSQL database. The service supports the following operations:

- `GET /questions` Fetch a list of questions. The returned list is paginated and be controlled using the the 'page' and 'size' request parameters. Defaults to the first page with a page size of 20 items.
- `POST /questions` Create a new question.
- `GET /questions/:id` Get an existing question
- `PUT /questions/:id` Updated an existing question
- `DELETE /questions/:id` Delete an existing question

The service is hosted on Heroku using the free hobby tier, which means it will be shut down after a period of no activity. It may take several seconds for a response on the first request while the instance is started and the data is loaded into the database.
