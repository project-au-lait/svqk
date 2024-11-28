package dev.aulait.svqk.interfaces.issue;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class IssueUpdateDto {

  @Schema(required = true)
  private IssueDto issue;

  @Schema(required = true)
  private JournalDto journal;
}
