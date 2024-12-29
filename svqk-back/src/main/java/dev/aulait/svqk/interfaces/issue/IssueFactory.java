package dev.aulait.svqk.interfaces.issue;

import static dev.aulait.svqk.arch.search.ArithmeticOperatorCd.*;
import static dev.aulait.svqk.arch.search.LogicalOperatorCd.*;

import dev.aulait.svqk.arch.search.SearchCriteriaBuilder;
import dev.aulait.svqk.arch.search.SearchCriteriaVo;
import dev.aulait.svqk.arch.search.SearchResultVo;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.arch.util.BeanUtils.MappingConfig;
import dev.aulait.svqk.domain.issue.IssueEntity;
import dev.aulait.svqk.domain.issue.IssueStatusEntity;
import dev.aulait.svqk.domain.issue.IssueTrackingRs;
import dev.aulait.svqk.interfaces.issue.IssueController.IssueSearchResultDto;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class IssueFactory {

  private MappingConfig<IssueEntity, IssueDto> searchResultConfig =
      BeanUtils.buildConfig(IssueEntity.class, IssueDto.class)
          .skip(IssueDto::setJournals)
          .build(); // <.>

  public SearchCriteriaVo build(IssueSearchCriteriaDto criteria) { // <.>
    SearchCriteriaBuilder builder =
        new SearchCriteriaBuilder()
            .select("SELECT i FROM IssueEntity i")
            .select("JOIN FETCH i.issueStatus s")
            .select("JOIN FETCH i.tracker t")
            .where("i.subject", LIKE, criteria.getText()); // <.>

    if (!criteria.isSubjectOnly()) {
      builder.where(OR, "i.description", LIKE, criteria.getText());
    }

    var statuses = BeanUtils.mapAll(criteria.getIssueStatuses(), IssueStatusEntity.class);

    return builder
        .where("i.issueStatus", IN, statuses)
        .where("i.dueDate", criteria.getDueDate())
        .defaultOrderBy("i.id", false)
        .build(criteria); // <.>
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

  public IssueSearchResultDto build(SearchResultVo<IssueEntity> vo) { // <.>
    return BeanUtils.map(searchResultConfig, vo, IssueSearchResultDto.class); // <.>
  }
}
