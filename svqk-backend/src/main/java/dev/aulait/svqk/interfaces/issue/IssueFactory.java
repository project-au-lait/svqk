package dev.aulait.svqk.interfaces.issue;

import static dev.aulait.sqb.ComparisonOperator.*;
import static dev.aulait.sqb.LogicalOperator.*;

import dev.aulait.sqb.LikePattern;
import dev.aulait.sqb.SearchCriteria;
import dev.aulait.sqb.SearchCriteriaBuilder;
import dev.aulait.sqb.SearchResult;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.arch.util.BeanUtils.MappingConfig;
import dev.aulait.svqk.domain.issue.IssueEntity;
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

  public SearchCriteria build(IssueSearchCriteriaDto criteria) { // <.>
    Object text = LikePattern.contains(criteria.getText());

    return new SearchCriteriaBuilder()
        .select("SELECT i FROM IssueEntity i")
        .select("JOIN FETCH i.issueStatus")
        .select("JOIN FETCH i.tracker")
        .where("i.subject", LIKE, text)
        .where(OR, "i.description", LIKE, criteria.isSubjectOnly() ? null : text)
        .where("i.issueStatus.id", IN, criteria.getIssueStatuses())
        .where("i.dueDate", criteria.getDueDate())
        .defaultOrderBy("i.id", false)
        .build(criteria.getPageControl()); // <.>
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

  public IssueSearchResultDto build(SearchResult<IssueEntity> vo) { // <.>
    return BeanUtils.map(searchResultConfig, vo, IssueSearchResultDto.class); // <.>
  }
}
