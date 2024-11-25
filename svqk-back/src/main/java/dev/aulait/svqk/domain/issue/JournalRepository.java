package dev.aulait.svqk.domain.issue;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<JournalEntity, Integer> {

  public List<JournalEntity> findByIssueIdOrderByCreatedAt(int issueId);
}
