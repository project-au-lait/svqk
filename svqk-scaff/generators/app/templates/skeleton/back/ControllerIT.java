<%_
pkFields = compositePk?.fields ?? [idField];
getMethodArgs = pkFields.map((f) => `id.get${f.fieldNmPascal}()`).join(', ');
// buildGetMethodArg = (field) => `${field.javaType} ${field.fieldName}`;

-%>
package <%= interfacesPkgNm %>;

import static org.junit.jupiter.api.Assertions.assertEquals;

<%_ if(compositePk) { -%>
import <%= domainPkgNm %>.<%= entityNmPascal %>EntityId;
<%_ } -%>
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

/**
 * This integration test is automatically generated.
 * 
 * The test generated is a sample that only checks the id.
 * Tests will fail depending on the entity configuration.
 * 
 * Change the content of the test if necessary.
 */
@QuarkusIntegrationTest
class <%= entityNmPascal %>ControllerIT {

  <%= entityNmPascal %>Client client = new <%= entityNmPascal %>Client();

  @Test
  void testCrud() {
    <%= entityNmPascal %>Dto dto = <%= entityNmPascal %>DataFactory.create<%= entityNmPascal %>();

    <%= idJavaType %> id =
      <%_ if (compositePk) { -%>
        <%= idJavaType %>.builder()
          <%_ compositePk.fields.forEach(function(pkField) { -%>
            .<%= pkField.fieldName %>(dto.get<%= pkField.fieldNmPascal %>())
          <%_ }); -%>
            .build();
      <%_ } else { -%>
        dto.get<%= idField.fieldNmPascal %>();
      <%_ } -%>

    // Create
    <%= idJavaType %> createdId = client.save(dto);
    assertEquals(id, createdId);

    // Reference
    <%= entityNmPascal %>Dto refDto = client.get(<%= getMethodArgs %>);
    <%= idJavaType %> refDtoId = 
      <%_ if (compositePk) { -%>
        <%= idJavaType %>.builder()
          <%_ compositePk.fields.forEach(function(pkField) { -%>
            .<%= pkField.fieldName %>(refDto.get<%= pkField.fieldNmPascal %>())
          <%_ }); -%>
            .build();
      <%_ } else { -%>
        refDto.get<%= idField.fieldNmPascal %>();
      <%_ } -%>
    assertEquals(id, refDtoId);
  }
}