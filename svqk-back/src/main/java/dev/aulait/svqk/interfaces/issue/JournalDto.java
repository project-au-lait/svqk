package dev.aulait.svqk.interfaces.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class JournalDto implements Comparable<JournalDto> {

  @JsonProperty("issueId")
  @Schema(required = true)
  private Integer idIssueId;

  @JsonProperty("seqNo")
  @Schema(required = true, readOnly = true)
  private Integer idSeqNo;

  private String notes;

  @Schema(required = true)
  private Long version;

  @Override
  public int compareTo(JournalDto o) {
    return idSeqNo.compareTo(o.idSeqNo);
  }
}
