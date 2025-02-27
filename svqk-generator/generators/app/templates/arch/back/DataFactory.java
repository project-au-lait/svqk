<%_ include('../../lib/data-factory-def', { fields, compIdFields }); -%>
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
      <%_ fields.forEach((field) => { -%>
        <%_ if (compIdFields && field.id) { -%>
        .<%= field.fieldName %>(<%= entityNmPascal %>IdDto.builder()
          <%_ compIdFields.forEach((compIdField) => { -%>
            .<%= compIdField.fieldName %>(<%= getValueCode(compIdField) %>)
          <%_ }); -%>
            .build())
        <%_ } else { -%>
        .<%= field.fieldName %>(<%= getValueCode(field) %>)
        <%_ } -%>
      <%_ }); -%>
        .build();
  }
}
