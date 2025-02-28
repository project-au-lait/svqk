package dev.aulait.svqk.domain.issue;

import static dev.aulait.svqk.arch.jpa.JpaUtils.findWithFetch;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IssueRepository extends JpaRepository<IssueEntity, Integer> {

  default IssueEntity findByIdWithDetails(Integer id, EntityManager em) {
    return findWithFetch(em, IssueEntity.class, id);
  }

  Optional<IssueEntity> findByIdAndVersion(int id, int version);

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
