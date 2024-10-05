package dev.aulait.svqk.interfaces.issue;

import static dev.aulait.svqk.arch.search.ConjunctionCd.*;
import static dev.aulait.svqk.arch.search.OperatorCd.*;

import dev.aulait.svqk.arch.search.SearchConditionBuilder;
import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.domain.issue.IssueStatusEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IssueFactory {

  public SearchConditionVo build(IssueSearchConditionDto cond) {
    SearchConditionBuilder builder = new SearchConditionBuilder();

    builder.where("subject", LIKE, OR, cond.getText());

    if (!cond.isSubjectOnly()) {
      builder.where("description", LIKE, OR, cond.getText());
    }

    var statuses = BeanUtils.mapAll(cond.getIssueStatuses(), IssueStatusEntity.class);
    builder.where("issueStatus", IN, statuses);

    builder.where("dueDate", cond.getDueDate());

    builder.defaultOrderBy("id", false);

    return builder.build(cond);
  }
}
