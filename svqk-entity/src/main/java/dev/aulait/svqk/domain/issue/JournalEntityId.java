package dev.aulait.svqk.domain.issue;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Generated("dev.aulait.jeg:jpa-entity-generator")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Embeddable
public class JournalEntityId implements java.io.Serializable {

  @Column(name = "issue_id")
  private Integer issueId;

  @Column(name = "seq_no")
  private Integer seqNo;
}
