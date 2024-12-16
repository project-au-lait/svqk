package dev.aulait.svqk.arch.front;

import dev.aulait.svqk.arch.util.JsonUtils;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import lombok.RequiredArgsConstructor;

@Path(FrontController.FRONT_PATH)
@RequiredArgsConstructor
public class FrontController {

  private final FrontConfigDto config;

  public static final String FRONT_PATH = "/front";

  @GET
  @Produces("text/javascript")
  public String get() {
    return "export const env=" + JsonUtils.obj2json(config);
  }
}
