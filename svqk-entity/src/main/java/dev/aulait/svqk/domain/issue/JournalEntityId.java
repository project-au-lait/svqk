package dev.aulait.svqk.domain.issue;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class JournalEntityId implements java.io.Serializable {

  @Column(name = "id")
  private Integer id;

  @Column(name = "issue_id")
  private Integer issueId;
}
