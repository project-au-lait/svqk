package dev.aulait.svqk.interfaces.tracker;

import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.arch.web.ApiPath;
import dev.aulait.svqk.domain.tracker.TrackerService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Path(TrackerController.TRACKERS_PATH)
@RequiredArgsConstructor
public class TrackerController {

  private final TrackerService service;

  static final String TRACKERS_PATH = ApiPath.ROOT + "/tracker";

  @GET
  public List<TrackerDto> getAllTrackers() {
    return BeanUtils.mapAll(service.findAll(), TrackerDto.class);
  }
}