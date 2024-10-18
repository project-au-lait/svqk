package dev.aulait.svqk.interfaces.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
public class IssueTrackingDto {

  @Schema(required = true)
  private TrackerDto tracker;

  @Schema(required = true)
  private List<IssueTrackingStatusDto> issueStatuses;

  @Schema(required = true)
  @JsonProperty
  private Integer total() {
    return issueStatuses.stream().mapToInt(IssueTrackingStatusDto::getCount).sum();
  }
}
