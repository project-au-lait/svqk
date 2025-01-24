package <%= interfacesPkgNm %>;

import dev.aulait.svqk.arch.search.PageControlDto;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class <%= entityNmPascal %>SearchCriteriaDto {
  @Schema(required = true)
  private PageControlDto pageControl = new PageControlDto();
}