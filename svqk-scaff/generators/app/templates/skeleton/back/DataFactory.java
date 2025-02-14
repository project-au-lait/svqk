<%_
dtoFields = (compositePk?.fields ?? []).concat(
  fields.filter((f) => !compositePk || (compositePk && !f.id))
  );
-%>
<%_ include('../../lib/data-factory-def', { fields:dtoFields }); -%>
package <%= interfacesPkgNm %>;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
<% imports.forEach((_import) => { -%>
import <%= _import %>;
<% }); -%>

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class <%= entityNmPascal %>DataFactory {

  public static <%= entityNmPascal %>Dto create<%= entityNmPascal %>() {
    return <%= entityNmPascal %>Dto.builder()
      <%_ dtoFields.forEach((field) => { -%>
        .<%= field.fieldName %>(<%= getValueCode(field) %>)
      <%_ }); -%>
        .build();
  }
}
