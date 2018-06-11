package io.durbs.scores.repo;

import io.durbs.scores.domain.Score;

import java.util.Set;

public interface ScoreRepository extends PurgingRepository {

    /**
     *
     * @param examId
     * @return
     */
    Set<Score> getAllScoresForExamId(Integer examId);

    /**
     *
     * @param studentId
     * @return
     */
    Set<Score> getScoresForStudentId(String studentId);

    /**
     *
     * @return
     */
    Set<Integer> getExamIds();

    /**
     *
     * @return
     */
    Set<String> getStudentIds();

    /**
     *
     * @param score
     */
    void saveScore(Score score);

}
