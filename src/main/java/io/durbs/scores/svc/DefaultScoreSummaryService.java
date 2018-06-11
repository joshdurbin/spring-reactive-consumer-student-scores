package io.durbs.scores.svc;

import io.durbs.scores.domain.Score;
import io.durbs.scores.domain.ScoreSummary;
import io.durbs.scores.repo.ScoreRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class DefaultScoreSummaryService implements ScoreSummaryService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Override
    public Optional<ScoreSummary> getSummaryForExamId(Integer examId) {

        final Optional<ScoreSummary> summary;

        if (scoreRepository.getExamIds().contains(examId)) {

            val scores = scoreRepository.getAllScoresForExamId(examId);

            val averageScore = scores.stream()
                    .mapToDouble(Score::getScore)
                    .average();

            summary = Optional.of(new ScoreSummary(scores, averageScore.getAsDouble()));

        } else {
            summary = Optional.empty();
        }

        return summary;
    }

    @Override
    public Optional<ScoreSummary> getSummaryForStudentId(String studentId) {

        final Optional<ScoreSummary> summary;

        if (scoreRepository.getStudentIds().contains(studentId)) {

            val scores = scoreRepository.getScoresForStudentId(studentId);

            val averageScore = scores.stream()
                    .mapToDouble(Score::getScore)
                    .average();

            summary = Optional.of(new ScoreSummary(scores, averageScore.getAsDouble()));

        } else {
            summary = Optional.empty();
        }

        return summary;
    }
}
