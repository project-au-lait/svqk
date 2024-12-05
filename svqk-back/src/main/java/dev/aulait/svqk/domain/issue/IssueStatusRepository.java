package dev.aulait.svqk.domain.issue;

import org.springframework.data.jpa.repository.JpaRepository;

interface IssueStatusRepository extends JpaRepository<IssueStatusEntity, Integer> {
}
