package dev.aulait.svqk.domain.issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JournalRepository extends JpaRepository<JournalEntity, JournalEntityId> {

  @Query(
      value =
          "SELECT COALESCE(MAX(journal.id.seqNo), 0) + 1"
              + " FROM JournalEntity journal"
              + " WHERE journal.id.issueId = :issueId")
  public Integer nextSeqNoByIssueId(Integer issueId);
}
