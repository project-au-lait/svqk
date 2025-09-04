package <%= interfacesPkgNm %>;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class <%= entityNmPascal %>Dto {
<%_ fields.forEach(field => { -%>

  <%_ if (field.required) { -%>
  @Schema(required = true)
  <%_ } -%>
  <%_ if (compIdFields && field.id) { -%>
  @Schema(required = true)
  private <%= entityNmPascal %>IdDto <%= field.fieldName %>;
  <%_ } else if (field.multiple) { -%>
  @Builder.Default
  private List<<%= field.javaType %>> <%= field.fieldName %>;
  <%_ } else { -%>
  private <%- field.javaType %> <%= field.fieldName %>;
  <%_ } -%>
<%_ }); -%>
}
