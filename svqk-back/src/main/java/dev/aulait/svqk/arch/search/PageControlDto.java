package dev.aulait.svqk.arch.search;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageControlDto {
  @Schema(required = true)
  @Builder.Default
  private int pageSize = 10;

  @Schema(required = true)
  @Builder.Default
  private int pageNumber = 1;

  @Builder.Default private int pageNumsRange = 2;

  @Builder.Default private List<SortOrderDto> sortOrders = new ArrayList<>();

  public int getOffset() {
    return (pageNumber - 1) * pageSize;
  }
}
