package dev.aulait.svqk.domain.issue;

import org.springframework.data.jpa.repository.JpaRepository;

interface IssueRepository extends JpaRepository<IssueEntity, Integer> {
}
