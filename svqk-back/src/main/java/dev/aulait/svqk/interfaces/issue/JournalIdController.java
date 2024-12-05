package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.domain.issue.JournalIdEntity;
import dev.aulait.svqk.domain.issue.JournalIdService;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Path(JournalIdController.JOURNALID_PATH)
@RequiredArgsConstructor
public class JournalIdController {

  private final JournalIdService journalIdService;

  static final String JOURNALID_PATH = ApiPath.ROOT + "/journalId";

  static final String JOURNALID_GET_PATH = "{id}";

  @GET
  @Path(JOURNALID_GET_PATH)
  public JournalIdDto get(@PathParam("id") int id) {

    JournalIdEntity entity = journalIdService.find(id);

    return JournalIdDto.builder().id(entity.getId()).message(entity.getMessage()).build();
  }

  @POST
  public int save(@Valid JournalIdDto dto) {

    JournalIdEntity entity = new JournalIdEntity();
    entity.setId(dto.getId());
    entity.setMessage(dto.getMessage());

    JournalIdEntity savedEntity = journalIdService.save(entity);

    return savedEntity.getId();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  static class ApiPath {
    static final String ROOT = "/api/v1";
  }
}
