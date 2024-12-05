package dev.aulait.svqk.domain.issue;

import org.springframework.data.jpa.repository.JpaRepository;

interface TrackerRepository extends JpaRepository<TrackerEntity, Integer> {
}
