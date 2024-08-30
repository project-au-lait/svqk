package dev.aulait.svqk.arch.jpa;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

public class BaseEntityListener {

  private static final String DEFAULT_USER = "anonymous";

  @PrePersist
  public void prePresist(BaseEntity entity) {
    LocalDateTime now = LocalDateTime.now();
    entity.setCreatedAt(now);
    entity.setCreatedBy(DEFAULT_USER);

    preUpdate(entity);
  }

  @PreUpdate
  public void preUpdate(BaseEntity entity) {
    entity.setUpdatedAt(LocalDateTime.now());
    entity.setUpdatedBy(DEFAULT_USER);
  }
}
