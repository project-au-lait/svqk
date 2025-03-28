package <%= interfacesPkgNm %>;

import dev.aulait.sqb.PageControl;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class <%= entityNmPascal %>SearchCriteriaDto {
  @Schema(required = true)
  private PageControl pageControl = new PageControl();
}
