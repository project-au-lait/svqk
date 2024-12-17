package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.domain.issue.TrackerService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Path(TrackerController.TRACKERS_PATH)
@RequiredArgsConstructor
public class TrackerController {

  private final TrackerService service;

  static final String TRACKERS_PATH = "tracker";

  @GET
  public List<TrackerDto> getAllTrackers() {
    return BeanUtils.mapAll(service.findAll(), TrackerDto.class);
  }
}
