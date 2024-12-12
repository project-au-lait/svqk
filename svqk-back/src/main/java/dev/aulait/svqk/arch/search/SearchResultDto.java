package dev.aulait.svqk.arch.search;

import java.util.List;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class SearchResultDto<T> {

  @Schema(required = true)
  private List<T> list;

  @Schema(required = true)
  private PageControlDto pageCtrl;
}
