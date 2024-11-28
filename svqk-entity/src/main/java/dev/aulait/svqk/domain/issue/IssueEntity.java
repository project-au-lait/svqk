package dev.aulait.svqk.domain.issue;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
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

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "issue_id", insertable = false, updatable = false)
  private Set<JournalEntity> journals = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tracker_id")
  private TrackerEntity tracker;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "status_id")
  private IssueStatusEntity issueStatus;
}
