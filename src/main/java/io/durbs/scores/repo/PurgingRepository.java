package io.durbs.scores.repo;

public interface PurgingRepository {

    /**
     * Removes all data from repo.
     *
     */
    void purge();
}
