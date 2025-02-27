package dev.aulait.svqk.arch.jpa;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;

@Path("jpql")
@AllArgsConstructor
public class JpqlExecController {

  private final EntityManager em;

  @GET
  public Object exec(@QueryParam("q") String query) {
    return em.createQuery(query).getResultList();
  }
}
