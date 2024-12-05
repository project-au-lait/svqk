package dev.aulait.svqk.domain.issue;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class JournalService {

  private final JournalRepository journalRepository;

  public JournalEntity find(int id) {

    return journalRepository.findById(id).orElseThrow(IllegalArgumentException::new);
  }

  @Transactional
  public JournalEntity save(JournalEntity entity) {

    return journalRepository.save(entity);
  }
}
