package dev.aulait.svqk.arch.jpa;

import dev.aulait.svqk.arch.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JpaRepositoryHandler {

  public static <T, I> T findByIdAsResource(JpaRepository<T, I> repository, I id) {
    return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
  }
}
