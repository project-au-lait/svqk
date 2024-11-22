package dev.aulait.svqk.interfaces.issue;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class JournalDto {

  @Schema(required = true, readOnly = true)
  private Integer id;

  @Schema(required = true)
  private Integer issueId;

  private String notes;

  @Schema(required = true, readOnly = true)
  private long version;
}
