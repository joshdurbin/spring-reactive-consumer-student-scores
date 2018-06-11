package io.durbs.scores.svc;

import io.durbs.scores.domain.ScoreSummary;

import java.util.Optional;

public interface ScoreSummaryService {

    /**
     *
     * @param examId
     * @return
     */
    Optional<ScoreSummary> getSummaryForExamId(Integer examId);

    /**
     *
     * @param studentId
     * @return
     */
    Optional<ScoreSummary> getSummaryForStudentId(String studentId);
}
