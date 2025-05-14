package <%= interfacesPkgNm %>;

import dev.aulait.sqb.PageControl;
import dev.aulait.sqb.SortOrder;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class <%= entityNmPascal %>SearchCriteriaDto {
  @Schema(required = true)
  private PageControl pageControl = new PageControl();

  private List<SortOrder> sortOrders = new ArrayList<>();
}
