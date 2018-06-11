package io.durbs.scores;

import io.durbs.scores.domain.ScoreSummary;
import io.durbs.scores.repo.ScoreRepository;
import io.durbs.scores.svc.ScoreSummaryService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Set;

@RestController
public class ScoreController {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private ScoreSummaryService scoreSummaryService;

    @PostMapping("/purge")
    public RedirectView purge() {

        scoreRepository.purge();

        return new RedirectView("/students");
    }

    @GetMapping("/students")
    public ResponseEntity<Set<String>> getStudents() {

        return ResponseEntity.ok(scoreRepository.getStudentIds());
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ScoreSummary> getStudentExamSummary(@PathVariable("id") final String id) {

        final ResponseEntity<ScoreSummary> responseEntity;

        val optionalExamSummary = scoreSummaryService.getSummaryForStudentId(id);

        if (optionalExamSummary.isPresent()) {
            responseEntity = ResponseEntity.ok(optionalExamSummary.get());
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @GetMapping("/exams")
    public ResponseEntity<Set<Integer>> getExams() {

        return ResponseEntity.ok(scoreRepository.getExamIds());
    }

    @GetMapping("/exams/{id}")
    public ResponseEntity<ScoreSummary> getSummaryForExamId(@PathVariable("id") final String id) {

        final ResponseEntity<ScoreSummary> responseEntity;

        val optionalExamSummary = scoreSummaryService.getSummaryForExamId(Integer.valueOf(id));

        if (optionalExamSummary.isPresent()) {
            responseEntity = ResponseEntity.ok(optionalExamSummary.get());
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }
}
