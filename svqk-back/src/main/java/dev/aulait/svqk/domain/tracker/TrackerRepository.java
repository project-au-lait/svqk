package dev.aulait.svqk.domain.tracker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackerRepository extends JpaRepository<TrackerEntity, Integer> {
}