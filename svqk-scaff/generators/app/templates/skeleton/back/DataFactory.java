<%_ include('../../lib/data-factory-def', { fields: fields.concat(compositePk?.fields) }); -%>
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
        <%_ if (compositePk && field.id) { -%>
        .<%= field.fieldName %>(<%= entityNmPascal %>IdDto.builder()
          <%_ compositePk.fields.forEach((pkField) => { -%>
            .<%= pkField.fieldName %>(<%= getValueCode(pkField) %>)
          <%_ }); -%>
            .build())
        <%_ } else { -%>
        .<%= field.fieldName %>(<%= getValueCode(field) %>)
        <%_ } -%>
      <%_ }); -%>
        .build();
  }
}
