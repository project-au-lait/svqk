package dev.aulait.svqk.domain.issue;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class JournalService {

  private final JournalRepository repository;

  // @Transactional
  // public JournalEntity save(JournalEntity entity) {
  //   return repository.save(entity);
  // }

  public List<JournalEntity> findByIssueId(int issueId) {
    return repository.findByIssueIdOrderByCreatedAt(issueId);
  }
}
