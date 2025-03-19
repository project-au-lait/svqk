<% include('../../lib/interface-common'); -%>
<%_
clientGetArgs = compIdFields ? ifcom.buildArgs((field) => `id.get${field.fieldNmPascal}()`) : "id";
-%>
package <%= interfacesPkgNm %>;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    <%= ifcom.interfaceIdType %> id = dto.get<%= idField.fieldNmPascal %>();

    // Create
    <%= ifcom.interfaceIdType %> createdId = client.save(dto);
    assertEquals(id, createdId);

    // Reference
    <%= entityNmPascal %>Dto refDto = client.get(<%= clientGetArgs %>);
    assertEquals(id, refDto.get<%= idField.fieldNmPascal %>());
  }
}