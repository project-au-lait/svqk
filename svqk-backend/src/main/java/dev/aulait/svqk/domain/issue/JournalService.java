package dev.aulait.svqk.domain.issue;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@ApplicationScoped
@RequiredArgsConstructor
public class JournalService {

  private final JournalRepository repository;

  @Transactional
  public JournalEntity save(JournalEntity entity) {

    // After the implementation of JournalDetail, empty notes will be allowed.
    if (StringUtils.isEmpty(entity.getNotes())) return entity;

    Integer count = repository.countByIdIssueId(entity.getId().getIssueId()).intValue();
    entity.getId().setSeqNo(++count);

    return repository.save(entity);
  }
}
