package dev.aulait.svqk.domain.issue;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssueRepository extends JpaRepository<IssueEntity, Integer> {

  @Query(
      value =
          "SELECT tracker, issueStatus, COUNT(issue)"
              + " FROM TrackerEntity tracker"
              + " CROSS JOIN IssueStatusEntity issueStatus"
              + " LEFT JOIN IssueEntity issue"
              + " ON issue.tracker = tracker AND issue.issueStatus = issueStatus"
              + " GROUP BY tracker, issueStatus"
              + " ORDER BY tracker, issueStatus")
  public List<IssueTrackingRs> count4tracking();

  @Query(
      value = 
          "SELECT issue "
              + " FROM IssueEntity issue "
              + " LEFT JOIN FETCH issue.issueStatus "
              + " LEFT JOIN FETCH issue.tracker "
              + " LEFT JOIN FETCH issue.journals "
              + " WHERE issue.id = :id")
  IssueEntity findIssueWithDetailsById(@Param("id") int id);
}
