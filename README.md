# Student / Exam scores

This Java 8 / Spring-based app integrates with the SSE event stream and stores the data
per the instructions in the initial README.

[![CircleCI](https://circleci.com/gh/joshdurbin/spring-consumer-student-scores.svg?style=svg)](https://circleci.com/gh/joshdurbin/spring-consumer-student-scores)

## Setup

1. Install the Oracle Java 8 JDK
2. Install gradle
3. (optional) If you're viewing the code in Intelij, be sure into install the Lombok plugin *and* enable Annotation processing for the project.

## Run

1. Run `gradle bootRun`

## REST Access

1. GET endpoint at `/students` that lists all users that have received at least one test score (ex: `http://localhost:8080/students`)
2. GET endpoint at `/students/{id}` that lists the test results for the specified student, and provides the student's average score across all exams (ex: `http://localhost:8080/students/{id}`)
3. GET endpoint at `/exams` that lists all the exams that have been recorded (ex: `http://localhost:8080/exams`)
4. GET endpoint at `/exams/{number}` that lists all the results for the specified exam, and provides the average score across all students (ex: `http://localhost:8080/exams{id}`)

## Notes

* The usage of `val` in the code is shorthand via lombok allowing the immutable assignment of typed
variables (i.e. replacing `final String name = "josh";` with `val name = "josh";`.
* The score repo implementation uses non-thread safe data structures internally and locks to ensure
data integrity across concurrent requests
* Score calculation is extracted into its own class
* The endpoint instructions for `/students` suggest there's a chance data from the SSE "stream"
could contain students with no scores. In any case, a production version of this app would employ
some form of validation on the inbound data. I've annotated the Score object with what those
conditionals might be, though it's not tested or used anywhere in the app.
* The score provider runs in its own thread and is invoked by a separate component.

## Limitations

* The score provider isn't tested, though the calculations done by the methods in the repo are.
* If the network is down or the SSE endpoint is unreachable for an extended period of time the
score provider will fail. There is no current watchdog to ensure that the provider is running.
* Once the provider has successfully connected, if it experiences intermittent connectivity,
issues within the allowed timeout window for the rest client, it will resume operations with only
a brief import/ingestion delay.
