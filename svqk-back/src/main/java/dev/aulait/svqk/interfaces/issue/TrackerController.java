package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.domain.issue.TrackerEntity;
import dev.aulait.svqk.domain.issue.TrackerService;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Path(TrackerController.TRACKER_PATH)
@RequiredArgsConstructor
public class TrackerController {

  private final TrackerService trackerService;

  static final String TRACKER_PATH = ApiPath.ROOT + "/tracker";

  static final String TRACKER_GET_PATH = "{id}";

  @GET
  @Path(TRACKER_GET_PATH)
  public TrackerDto get(@PathParam("id") int id) {

    TrackerEntity entity = trackerService.find(id);

    return TrackerDto.builder().id(entity.getId()).message(entity.getMessage()).build();
  }

  @POST
  public int save(@Valid TrackerDto dto) {

    TrackerEntity entity = new TrackerEntity();
    entity.setId(dto.getId());
    entity.setMessage(dto.getMessage());

    TrackerEntity savedEntity = trackerService.save(entity);

    return savedEntity.getId();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  static class ApiPath {
    static final String ROOT = "/api/v1";
  }
}
