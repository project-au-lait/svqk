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
    return repository.save(entity);
  }
}
