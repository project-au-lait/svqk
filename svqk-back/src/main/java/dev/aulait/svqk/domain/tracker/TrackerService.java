package dev.aulait.svqk.domain.tracker;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class TrackerService {

  private final TrackerRepository repository;

  public List<TrackerEntity> findAll() {
    return repository.findAll();
  }

  public Optional<TrackerEntity> findById(String id) {
    return repository.findById(id);
  }

  public TrackerEntity createOrUpdateTracker(TrackerEntity entity) {
    return repository.save(entity);
  }

  public void deleteTracker(String id) {
    repository.deleteById(id);
  }
}
