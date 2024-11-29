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
import dev.aulait.svqk.interfaces.issue.IssueTrackingDto.IssueStatusCountDto;
import dev.aulait.svqk.interfaces.issue.IssueTrackingDto.TrackerCountDto;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    // key: trackerId
    Map<String, TrackerCountDto> trackerMap = new HashMap<>();
    Set<IssueStatusDto> issueStatuses = new HashSet<>();

    for (IssueTrackingRs issueTracking : issueTrackings) {
      TrackerCountDto trackerCount =
          trackerMap.computeIfAbsent(
              issueTracking.getTracker().getId(),
              key -> BeanUtils.map(issueTracking, TrackerCountDto.class));

      IssueStatusCountDto status = BeanUtils.map(issueTracking, IssueStatusCountDto.class);
      trackerCount.getIssueStatusMap().put(status.getIssueStatus().getId(), status);

      issueStatuses.add(status.getIssueStatus());
    }

    IssueTrackingDto dto = new IssueTrackingDto();
    dto.getTrackers().addAll(trackerMap.values().stream().toList());
    dto.getIssueStatuses().addAll(issueStatuses);

    return dto;
  }

  public IssueSearchResultDto build(SearchResultVo<IssueEntity> vo) {
    return BeanUtils.map(searchResultConfig, vo, IssueSearchResultDto.class);
  }
}
