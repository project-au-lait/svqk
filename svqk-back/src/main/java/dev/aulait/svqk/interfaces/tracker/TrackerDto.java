package dev.aulait.svqk.interfaces.tracker;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import lombok.Data;

@Data
public class TrackerDto {

  @Schema(required = true, readOnly = true)
  private Integer id;

  @Schema(required = true)
  private String name;
}