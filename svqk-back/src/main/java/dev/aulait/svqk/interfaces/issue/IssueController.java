package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.search.SearchResultDto;
import dev.aulait.svqk.arch.search.SearchResultVo;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.arch.web.ApiPath;
import dev.aulait.svqk.arch.web.IdDto;
import dev.aulait.svqk.domain.issue.IssueEntity;
import dev.aulait.svqk.domain.issue.IssueService;
import dev.aulait.svqk.domain.issue.JournalEntity;
import dev.aulait.svqk.domain.issue.JournalService;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Path(IssueController.ISSUES_PATH)
@RequiredArgsConstructor
public class IssueController {

  private final IssueService service;

  private final IssueFactory factory;

  private final JournalService journalService;

  private final JournalFactory journalFactory;

  static final String ISSUES_PATH = ApiPath.ROOT + "/issues";

  static final String ISSUES_GET_PATH = "{issueId}";

  static final String ISSUES_TRACKING_GET_PATH = "tracking";

  static final String ISSUES_SEARCH_PATH = "search";

  public static class IssueSearchResultDto extends SearchResultDto<IssueDto> {} // <.>

  @POST
  public IdDto save(@Valid IssueDto dto) { // <.>

    // <.>
    IssueEntity entity = BeanUtils.map(dto, IssueEntity.class);
    JournalEntity newJournal = journalFactory.buildNewJournal(dto.getId(), dto.getNewJournal());

    IssueEntity savedEntity = service.save(entity, newJournal);

    return BeanUtils.map(savedEntity, IdDto.class);
  }

  @GET
  @Path(ISSUES_GET_PATH)
  public IssueDto get(@PathParam("issueId") int id) {
    IssueEntity entity = service.find(id);
    List<JournalEntity> journals = journalService.findByIssueId(id);

    return factory.buildIssueDto(entity, journals);
  }

  @POST
  @Path(ISSUES_SEARCH_PATH)
  public IssueSearchResultDto search(IssueSearchConditionDto dto) { // <.>
    // <.>
    SearchConditionVo vo = factory.build(dto);
    SearchResultVo<IssueEntity> result = service.search(vo);

    return BeanUtils.map(result, IssueSearchResultDto.class);
  }

  @GET
  @Path(ISSUES_TRACKING_GET_PATH)
  public IssueTrackingDto getTracking() {
    return factory.buildIssueTracking(service.getTracking());
  }
}
