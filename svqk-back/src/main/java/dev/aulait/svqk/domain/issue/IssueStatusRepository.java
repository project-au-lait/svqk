package dev.aulait.svqk.domain.issue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueStatusRepository extends JpaRepository<IssueStatusEntity, String> { // <.>
}
