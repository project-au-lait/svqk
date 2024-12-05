package dev.aulait.svqk.domain.issue;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class IssueService {

  private final IssueRepository issueRepository;

  public IssueEntity find(int id) {

    return issueRepository.findById(id).orElseThrow(IllegalArgumentException::new);
  }

  @Transactional
  public IssueEntity save(IssueEntity entity) {

    return issueRepository.save(entity);
  }
}
