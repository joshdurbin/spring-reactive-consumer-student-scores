package io.durbs.scores.repo;

import io.durbs.scores.domain.Score;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DefaultScoreRepository implements ScoreRepository {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Map<String, Set<Score>> scoreDataByStudentId = new LinkedHashMap<>();
    private final Set<Integer> exams = new LinkedHashSet<>();

    @Override
    public Set<Score> getAllScoresForExamId(Integer examId) {

        final Set<Score> scores;

        try {

            readWriteLock.readLock().lock();

            scores = scoreDataByStudentId.values().stream()
                    .flatMap(Set::stream)
                    .filter(exam -> exam.getExam().equals(examId))
                    .collect(Collectors.toSet());

        } finally {

            readWriteLock.readLock().unlock();
        }

        return scores;

    }

    @Override
    public Set<Score> getScoresForStudentId(String studentId) {

        final Set<Score> scores;

        try {

            readWriteLock.readLock().lock();

            scores = scoreDataByStudentId.getOrDefault(studentId, new HashSet<>());

        } finally {

            readWriteLock.readLock().unlock();
        }

        return scores;
    }

    @Override
    public Set<Integer> getExamIds() {

        final Set<Integer> examIds;

        try {

            readWriteLock.readLock().lock();

            examIds = exams;

        } finally {

            readWriteLock.readLock().unlock();
        }

        return examIds;
    }

    @Override
    public Set<String> getStudentIds() {

        final Set<String> studentIds;

        try {

            readWriteLock.readLock().lock();

            studentIds = scoreDataByStudentId.keySet();

        } finally {

            readWriteLock.readLock().unlock();
        }

        return studentIds;
    }

    @Override
    public void saveScore(Score score) {

        log.debug("received score {}", score);

        try {

            readWriteLock.writeLock().lock();

            if (scoreDataByStudentId.containsKey(score.getStudentId())) {

                scoreDataByStudentId.get(score.getStudentId()).add(score);

            } else {

                val scoreSet = new ConcurrentSkipListSet<Score>();
                scoreSet.add(score);

                scoreDataByStudentId.put(score.getStudentId(), scoreSet);
            }

            exams.add(score.getExam());

        } finally {

            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void purge() {

        try {

            readWriteLock.writeLock().lock();

            scoreDataByStudentId.clear();
            exams.clear();

        } finally {

            readWriteLock.writeLock().unlock();
        }
    }
}
