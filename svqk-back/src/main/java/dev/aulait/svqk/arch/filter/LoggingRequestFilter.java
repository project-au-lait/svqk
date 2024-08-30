package dev.aulait.svqk.arch.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@PreMatching
@Provider
@Slf4j
public class LoggingRequestFilter implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext ctx) {
    log.info("Access: {} {}", ctx.getUriInfo().getAbsolutePath(), ctx.getMethod());
  }
}
