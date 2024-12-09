package dev.aulait.svqk.domain.issue;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class JournalEntityId implements java.io.Serializable {

  @Column(name = "issue_id")
  private Integer issueId;

  @Column(name = "seq_no")
  private Integer seqNo;
}
