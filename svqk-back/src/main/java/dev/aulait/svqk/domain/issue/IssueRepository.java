package dev.aulait.svqk.domain.issue;

import static dev.aulait.svqk.arch.jpa.JpaUtils.buildFetchGraph;

import dev.aulait.svqk.arch.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IssueRepository extends JpaRepository<IssueEntity, Integer> {

  default IssueEntity findByIdWithDetails(Integer id, EntityManager em) {
    return Optional.ofNullable(
            em.find(IssueEntity.class, id, buildFetchGraph(em, IssueEntity.class)))
        .orElseThrow(() -> new ResourceNotFoundException(id));
  }

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
}
