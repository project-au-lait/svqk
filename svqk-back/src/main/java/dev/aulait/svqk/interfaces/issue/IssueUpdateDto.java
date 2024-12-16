package dev.aulait.svqk.interfaces.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueUpdateDto {

  @Schema(required = true)
  private IssueDto issue;

  @Schema(required = true)
  private JournalDto journal;
}
