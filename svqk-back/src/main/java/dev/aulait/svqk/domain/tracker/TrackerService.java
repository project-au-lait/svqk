package dev.aulait.svqk.domain.tracker;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class TrackerService {

  private final TrackerRepository repository;

  public List<TrackerEntity> findAll() {
    return repository.findAll();
  }
}