package dev.aulait.svqk.interfaces.issue;

import static dev.aulait.svqk.arch.search.ConjunctionCd.*;
import static dev.aulait.svqk.arch.search.OperatorCd.*;

import dev.aulait.svqk.arch.search.SearchConditionBuilder;
import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.search.SearchResultVo;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.arch.util.BeanUtils.MappingConfig;
import dev.aulait.svqk.domain.issue.IssueEntity;
import dev.aulait.svqk.domain.issue.IssueStatusEntity;
import dev.aulait.svqk.domain.issue.IssueTrackingRs;
import dev.aulait.svqk.interfaces.issue.IssueController.IssueSearchResultDto;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped // <.>
public class IssueFactory {

  private MappingConfig<IssueEntity, IssueDto> searchResultConfig =
      BeanUtils.buildConfig(IssueEntity.class, IssueDto.class).skip(IssueDto::setJournals).build();

  public SearchConditionVo build(IssueSearchConditionDto cond) { // <.>
    SearchConditionBuilder builder =
        new SearchConditionBuilder()
            .from(IssueEntity.class)
            .join("issueStatus")
            .join("tracker")
            .where("subject", LIKE, cond.getText()); // <.>

    if (!cond.isSubjectOnly()) {
      builder.where(OR, "description", LIKE, cond.getText());
    }

    var statuses = BeanUtils.mapAll(cond.getIssueStatuses(), IssueStatusEntity.class);

    return builder
        .where("issueStatus", IN, statuses)
        .where("dueDate", cond.getDueDate())
        .defaultOrderBy("id", false)
        .build(cond); // <.>
  }

  public IssueTrackingDto buildIssueTracking(List<IssueTrackingRs> issueTrackings) {
    IssueTrackingDto dto = new IssueTrackingDto();

    for (IssueTrackingRs issueTracking : issueTrackings) {
      dto.add(
          issueTracking.getTracker().getId(),
          issueTracking.getIssueStatus().getId(),
          issueTracking.getCount());

      dto.add(issueTracking.getTracker().getId(), issueTracking.getCount());
    }

    return dto;
  }

  public IssueSearchResultDto build(SearchResultVo<IssueEntity> vo) {
    return BeanUtils.map(searchResultConfig, vo, IssueSearchResultDto.class);
  }
}
