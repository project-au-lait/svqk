package dev.aulait.svqk.domain.issue;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class JournalIdService {

  private final JournalIdRepository journalIdRepository;

  public JournalIdEntity find(int id) {

    return journalIdRepository.findById(id).orElseThrow(IllegalArgumentException::new);
  }

  @Transactional
  public JournalIdEntity save(JournalIdEntity entity) {

    return journalIdRepository.save(entity);
  }
}
