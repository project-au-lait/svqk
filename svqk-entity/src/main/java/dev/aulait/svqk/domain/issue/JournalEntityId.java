package dev.aulait.svqk.domain.issue;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class JournalEntityId implements java.io.Serializable {

  @Column(name = "issue_id")
  private Integer issueId;

  @Column(name = "seq_no")
  private Integer seqNo;
}
