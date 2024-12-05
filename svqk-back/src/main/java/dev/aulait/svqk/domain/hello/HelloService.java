package dev.aulait.svqk.domain.hello;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class HelloService {

  private final HelloRepository helloRepository;

  public HelloEntity find(int id) {

    return helloRepository.findById(id).orElseThrow(IllegalArgumentException::new);
  }

  @Transactional
  public HelloEntity save(HelloEntity entity) {

    return helloRepository.save(entity);
  }
}
