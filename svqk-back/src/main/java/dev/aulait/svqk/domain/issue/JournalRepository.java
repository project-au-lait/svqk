package dev.aulait.svqk.domain.issue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<JournalEntity, JournalEntityId> {

  public Long countByIdIssueId(Integer issueId);
}
