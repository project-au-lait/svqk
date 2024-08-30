package dev.aulait.svqk.arch.front;

import java.net.URL;

import io.vertx.ext.web.Router;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class FrontResourceRouter {

  public void init(@Observes Router router) {
    URL index = getClass().getResource("/META-INF/resources/webjars/front/index.html");
    if (index == null) {
      log.info("Front SPA is not installed.");
      return;
    }

    log.info("Front SPA is installed. (Entry file: )", index);

    router.get("/").handler(rc -> rc.reroute("/webjars/front/"));
    router.get("/favicon.png").handler(rc -> rc.reroute("/webjars/front/favicon.png"));
    router.get("/_app/env.js").handler(rc -> rc.reroute(FrontController.FRONT_PATH));
    router.get("/_app/*").handler(rc -> rc.reroute("/webjars/front" + rc.normalizedPath()));
    router.route().last().handler(rc -> rc.reroute("/webjars/front/"));
  }
}
