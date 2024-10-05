package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.search.SearchResultDto;
import dev.aulait.svqk.arch.search.SearchResultVo;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.arch.web.ApiPath;
import dev.aulait.svqk.arch.web.IdDto;
import dev.aulait.svqk.domain.issue.IssueEntity;
import dev.aulait.svqk.domain.issue.IssueService;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path(IssueController.ISSUES_PATH)
@RequiredArgsConstructor
public class IssueController {

  private final IssueService service;

  private final IssueFactory factory;

  static final String ISSUES_PATH = ApiPath.ROOT + "/issues";

  @SuppressWarnings("java:S1075")
  static final String ISSUES_GET_PATH = "/{issueId}";

  public static class IssueSearchResultModel extends SearchResultDto<IssueDto> {}

  @POST
  public IdDto save(@RequestBody @Valid IssueDto dto) {

    IssueEntity entity = BeanUtils.map(dto, IssueEntity.class);
    IssueEntity savedEntity = service.save(entity);

    return BeanUtils.map(savedEntity, IdDto.class);
  }

  @GET
  @Path(ISSUES_GET_PATH)
  public IssueDto get(@PathParam("issueId") int id) {
    IssueEntity entity = service.find(id);

    return BeanUtils.map(entity, IssueDto.class);
  }

  @POST
  @Path("/search")
  public IssueSearchResultModel search(IssueSearchConditionDto dto) {
    SearchConditionVo vo = factory.build(dto);
    SearchResultVo<IssueEntity> result = service.search(vo);
    List<IssueDto> list = BeanUtils.mapAll(result.getList(), IssueDto.class);

    return SearchResultDto.build(vo, result, list, IssueSearchResultModel.class);
  }
}
