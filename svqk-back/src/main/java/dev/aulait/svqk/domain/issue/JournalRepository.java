package dev.aulait.svqk.domain.issue;

import org.springframework.data.jpa.repository.JpaRepository;

interface JournalRepository extends JpaRepository<JournalEntity, Integer> {
}
