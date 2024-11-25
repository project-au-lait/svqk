package dev.aulait.svqk.domain.issue;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class JournalService {

  private final JournalRepository repository;

  public List<JournalEntity> findByIssueId(int issueId) {
    return repository.findByIssueIdOrderByCreatedAt(issueId);
  }
}
