package dev.aulait.svqk.interfaces.issue;

import static dev.aulait.svqk.arch.search.ConjunctionCd.*;
import static dev.aulait.svqk.arch.search.OperatorCd.*;

import dev.aulait.svqk.arch.search.SearchConditionBuilder;
import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.domain.issue.IssueEntity;
import dev.aulait.svqk.domain.issue.IssueStatusEntity;
import dev.aulait.svqk.domain.issue.IssueTrackingIf;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@ApplicationScoped // <.>
public class IssueFactory {

  public SearchConditionVo build(IssueSearchConditionDto cond) { // <.>
    SearchConditionBuilder builder =
        new SearchConditionBuilder()
            .from(IssueEntity.class)
            .join("issueStatus")
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

  public List<IssueTrackingDto> createTrackingResponse(List<IssueTrackingIf> src) {
    List<IssueTrackingDto> result = new ArrayList<>();

    Map<String, List<IssueTrackingIf>> grpByTracker =
        src.stream()
            .collect(
                Collectors.groupingBy(
                    it -> it.getTracker().getId(), TreeMap::new, Collectors.toList()));

    grpByTracker.forEach(
        (k, v) -> {
          IssueTrackingDto dto = new IssueTrackingDto();
          dto.setTracker(BeanUtils.map(v.get(0).getTracker(), TrackerDto.class));
          dto.setIssueStatuses(BeanUtils.mapAll(v, IssueTrackingStatusDto.class));
          result.add(dto);
        });

    return result;
  }
}
