package dev.aulait.svqk.domain.issue;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class JournalService {

  private final JournalRepository repository;

  @Transactional
  public JournalEntity save(JournalEntity entity) {
    Integer nextSeqNo = repository.nextSeqNoByIssueId(entity.getId().getIssueId());
    entity.getId().setSeqNo(nextSeqNo);

    return repository.save(entity);
  }
}
