package dev.aulait.svqk.arch.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortOrderDto {
  @Schema(required = true)
  private boolean asc;

  @Schema(required = true)
  private String field;
}
