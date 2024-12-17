package dev.aulait.svqk.arch.front;

import io.vertx.ext.web.Router;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import java.net.URL;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Slf4j
public class FrontResourceRouter {

  @ConfigProperty(name = "quarkus.rest.path")
  Optional<String> restPath;

  public void init(@Observes Router router) {
    URL index = getClass().getResource("/META-INF/resources/webjars/front/index.html");
    if (index == null) {
      log.info("Front SPA is not installed.");
      return;
    }

    log.info("Front SPA is installed. (Entry file: {})", index);

    router.get("/").handler(rc -> rc.reroute("/webjars/front/"));
    router.get("/favicon.png").handler(rc -> rc.reroute("/webjars/front/favicon.png"));
    router
        .get("/_app/env.js")
        .handler(rc -> rc.reroute(restPath.orElse("") + FrontController.FRONT_PATH));
    router.get("/_app/*").handler(rc -> rc.reroute("/webjars/front" + rc.normalizedPath()));
    router.route().last().handler(rc -> rc.reroute("/webjars/front/"));
  }
}
