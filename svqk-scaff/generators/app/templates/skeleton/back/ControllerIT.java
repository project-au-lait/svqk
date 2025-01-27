<% include('../../lib/common-func'); -%>
<% include('../../lib/field-util', { fields }); -%>
package <%= interfacesPkgNm %>;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;
<% imports.forEach((_import) => { -%>
import <%= _import %>;
<% }); -%>

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
    int id = dto.get<%= toPascal(idFieldNm) %>();

    // Create
    int createdId = client.save(dto);
    assertEquals(id, createdId);

    // Reference
    <%= entityNmPascal %>Dto refDto = client.get(id);
    assertEquals(id, refDto.get<%= toPascal(idFieldNm) %>());
  }
}