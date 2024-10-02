package dev.aulait.svqk.domain.tracker;

import dev.aulait.svqk.domain.issue.IssueStatusEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tracker_issue_status")
public class TrackerIssueStatusEntity extends dev.aulait.svqk.arch.jpa.BaseEntity
    implements java.io.Serializable {

  @Id
  @Column(name = "id")
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "issue_status_id")
  private IssueStatusEntity issueStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tracker_id")
  private TrackerEntity tracker;
}
