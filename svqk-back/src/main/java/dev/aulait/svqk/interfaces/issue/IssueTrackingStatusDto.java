package dev.aulait.svqk.interfaces.issue;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class IssueTrackingStatusDto {

  @Schema(required = true)
  private IssueStatusDto issueStatus;

  @Schema(required = true)
  private Integer count;
}
