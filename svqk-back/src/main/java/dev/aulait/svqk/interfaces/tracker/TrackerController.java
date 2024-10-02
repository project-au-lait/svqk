package dev.aulait.svqk.interfaces.tracker;

import java.util.List;

import dev.aulait.svqk.arch.exception.ResourceNotFoundException;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.arch.web.ApiPath;
import dev.aulait.svqk.domain.tracker.TrackerEntity;
import dev.aulait.svqk.domain.tracker.TrackerService;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.RequiredArgsConstructor;

@Path(TrackerController.TRACKERS_PATH)
@RequiredArgsConstructor
public class TrackerController {

  private final TrackerService service;

  static final String TRACKERS_PATH = ApiPath.ROOT + "/tracker";

  @SuppressWarnings("java:S1075")
  static final String TRACKER_GET_PATH = "/{trackerId}";

  @GET
  public List<TrackerDto> getAllTrackers() {
    List<TrackerEntity> entities = service.findAll();
    return BeanUtils.mapAll(entities, TrackerDto.class);
  }

  @POST
  public TrackerDto createTracker(@Valid TrackerDto trackerDto) {

    TrackerEntity entity = BeanUtils.map(trackerDto, TrackerEntity.class);
    TrackerEntity savedEntity = service.createOrUpdateTracker(entity);

    return BeanUtils.map(savedEntity, TrackerDto.class);
  }

  @GET
  @Path(TRACKER_GET_PATH)
  public TrackerDto getTrackerById(@PathParam("trackerId") String id) {

    TrackerEntity entity = service.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tracker not found with id: " + id));

    return BeanUtils.map(entity, TrackerDto.class);
  }

  @PUT
  @Path(TRACKER_GET_PATH)
  public TrackerDto updateTracker(@PathParam("trackerId") String id, @Valid TrackerDto trackerDto) {

    if (!id.equals(trackerDto.getId())) {
      throw new BadRequestException("trackerId mismatch");
    }

    TrackerEntity entity = BeanUtils.map(trackerDto, TrackerEntity.class);
    TrackerEntity updatedEntity = service.createOrUpdateTracker(entity);

    return BeanUtils.map(updatedEntity, TrackerDto.class);
  }

  @DELETE
  @Path(TRACKER_GET_PATH)
  public void deleteTracker(@PathParam("trackerId") String id) {
    service.deleteTracker(id);
  }
}
