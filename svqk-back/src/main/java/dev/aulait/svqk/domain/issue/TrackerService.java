package dev.aulait.svqk.domain.issue;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class TrackerService {

  private final TrackerRepository repository;

  public List<TrackerEntity> findAll() {
    return repository.findAll();
  }

  public TrackerEntity findById(Integer id) {
    return repository.findById(id).orElse(null);
  }
}
