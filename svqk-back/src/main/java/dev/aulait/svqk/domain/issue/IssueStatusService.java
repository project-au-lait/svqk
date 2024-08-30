package dev.aulait.svqk.domain.issue;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped // <.>
@RequiredArgsConstructor // <.>
public class IssueStatusService {

  private final IssueStatusRepository statusRepository; // <.>

  public List<IssueStatusEntity> findAll() {
    return statusRepository.findAll(); // <.>
  }

}
