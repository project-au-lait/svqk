package dev.aulait.svqk.interfaces.issue;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class TrackerDto {

  @Schema(required = true, readOnly = true)
  private Integer id;

  @Schema(required = true)
  private String name;
}
