package dev.aulait.svqk.domain.issue;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class IssueStatusService {

  private final IssueStatusRepository issueStatusRepository;

  public IssueStatusEntity find(int id) {

    return issueStatusRepository.findById(id).orElseThrow(IllegalArgumentException::new);
  }

  @Transactional
  public IssueStatusEntity save(IssueStatusEntity entity) {

    return issueStatusRepository.save(entity);
  }
}
