package dev.aulait.svqk.arch.jpa;

import dev.aulait.sqb.jpa.JpaSearchQueryExecutor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class Producer {

  @Produces
  public JpaSearchQueryExecutor searchExecutor() {
    return new JpaSearchQueryExecutor();
  }
}
