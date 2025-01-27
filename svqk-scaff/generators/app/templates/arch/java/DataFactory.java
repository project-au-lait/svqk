<% include('../../lib/common-func'); -%>
<% include('../../lib/field-util', { fields }); -%>
package <%= interfacesPkgNm %>;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

<% imports.forEach((_import) => { -%>
  import <%= _import %>;
  <% }); -%>

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class <%= entityNmPascal %>DataFactory {

  public static <%= entityNmPascal %>Dto create<%= entityNmPascal %>() {
    <%= entityNmPascal %>Dto dto =
        <%= entityNmPascal %>Dto.builder()
        <%_ fields.forEach((field) => { -%>
            .<%= field.fieldName %>(<%= getValueCode(field) %>)
        <%_ }); -%>
            .build();

    return dto;
  }
}
