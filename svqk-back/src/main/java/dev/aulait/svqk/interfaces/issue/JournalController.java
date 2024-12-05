package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.domain.issue.JournalEntity;
import dev.aulait.svqk.domain.issue.JournalService;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Path(JournalController.JOURNAL_PATH)
@RequiredArgsConstructor
public class JournalController {

  private final JournalService journalService;

  static final String JOURNAL_PATH = ApiPath.ROOT + "/journal";

  static final String JOURNAL_GET_PATH = "{id}";

  @GET
  @Path(JOURNAL_GET_PATH)
  public JournalDto get(@PathParam("id") int id) {

    JournalEntity entity = journalService.find(id);

    return JournalDto.builder().id(entity.getId()).message(entity.getMessage()).build();
  }

  @POST
  public int save(@Valid JournalDto dto) {

    JournalEntity entity = new JournalEntity();
    entity.setId(dto.getId());
    entity.setMessage(dto.getMessage());

    JournalEntity savedEntity = journalService.save(entity);

    return savedEntity.getId();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  static class ApiPath {
    static final String ROOT = "/api/v1";
  }
}
