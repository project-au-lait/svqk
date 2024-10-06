package dev.aulait.svqk.interfaces.issue;

import static dev.aulait.svqk.arch.search.ConjunctionCd.*;
import static dev.aulait.svqk.arch.search.OperatorCd.*;

import dev.aulait.svqk.arch.search.SearchConditionBuilder;
import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.domain.issue.IssueEntity;
import dev.aulait.svqk.domain.issue.IssueStatusEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IssueFactory {

  public SearchConditionVo build(IssueSearchConditionDto cond) {
    SearchConditionBuilder builder =
        new SearchConditionBuilder()
            .from(IssueEntity.class)
            .join("issueStatus")
            .where("subject", LIKE, cond.getText());

    if (!cond.isSubjectOnly()) {
      builder.where(OR, "description", LIKE, cond.getText());
    }

    var statuses = BeanUtils.mapAll(cond.getIssueStatuses(), IssueStatusEntity.class);

    return builder
        .where("issueStatus", IN, statuses)
        .where("dueDate", cond.getDueDate())
        .defaultOrderBy("id", false)
        .build(cond);
  }
}
