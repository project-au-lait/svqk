package dev.aulait.svqk.interfaces.issue;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import dev.aulait.svqk.interfaces.tracker.TrackerDto;

@Data // <.>
public class IssueDto {

  // <.>
  @Schema(required = true, readOnly = true)
  private int id;

  @Schema(required = true)
  @NotBlank
  private String subject;

  private String description;

  private LocalDate dueDate;

  @Schema(required = true)
  private IssueStatusDto issueStatus;

  @Schema(required = true)
  private TrackerDto tracker;

  @Schema(required = true)
  private long version;

  @Schema(required = true, readOnly = true)
  private LocalDateTime updatedAt;
}
