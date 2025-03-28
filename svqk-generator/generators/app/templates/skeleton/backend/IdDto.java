package <%= interfacesPkgNm %>;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class <%= entityNmPascal %>IdDto {
<%_ compIdFields.forEach((field) => { -%>

  <%_ if (field.required) { -%>
  @Schema(required = true)
  <%_ } -%>
  private <%= field.javaType %> <%= field.fieldName %>;
<%_ }); -%>
}
