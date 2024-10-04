package dev.aulait.svqk.domain.issue;

import dev.aulait.svqk.domain.tracker.TrackerEntity;
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
@Table(name = "issue")
public class IssueEntity extends dev.aulait.svqk.arch.jpa.BaseEntity
    implements java.io.Serializable {

  @Id
  @Column(name = "id")
  @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "subject")
  private String subject;

  @Column(name = "due_date")
  private java.time.LocalDate dueDate;

  @Column(name = "description")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "status_id")
  private IssueStatusEntity issueStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tracker_id")
  private TrackerEntity tracker;
}
