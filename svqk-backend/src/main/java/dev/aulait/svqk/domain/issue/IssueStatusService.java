package dev.aulait.svqk.domain.issue;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class IssueStatusService {

  private final IssueStatusRepository statusRepository;

  public List<IssueStatusEntity> findAll() { // <.>
    return statusRepository.findAll(); // <.>
  }
}
