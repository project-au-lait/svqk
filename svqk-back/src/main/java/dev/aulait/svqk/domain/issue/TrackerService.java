package dev.aulait.svqk.domain.issue;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class TrackerService {

  private final TrackerRepository trackerRepository;

  public TrackerEntity find(int id) {

    return trackerRepository.findById(id).orElseThrow(IllegalArgumentException::new);
  }

  @Transactional
  public TrackerEntity save(TrackerEntity entity) {

    return trackerRepository.save(entity);
  }
}
