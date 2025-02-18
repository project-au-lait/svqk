<%_
getMethodArgs = compIdFields 
  ? compIdFields.map((field) => `id.get${field.fieldNmPascal}()`).join(', ')
  : "id";

idJavaType = compIdFields ? `${entityNmPascal}IdDto` : idField.javaType;
-%>
package <%= interfacesPkgNm %>;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmPascal %>SearchResultDto;

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
    <%= idJavaType %> id = dto.get<%= idField.fieldNmPascal %>();

    // Create
    <%= idJavaType %> createdId = client.save(dto);
    assertEquals(id, createdId);

    // Reference
    <%= entityNmPascal %>Dto refDto = client.get(<%= getMethodArgs %>);
    assertEquals(id, refDto.get<%= idField.fieldNmPascal %>());

    // Update
    // TODO Implementation of assembling a request and assertion
    <%= idJavaType %> updatedId = client.update(dto);

    // Search
    <%= entityNmPascal %>SearchCriteriaDto criteria = new <%= entityNmPascal %>SearchCriteriaDto();
    <%= entityNmPascal %>SearchResultDto result = client.search(criteria);
    assertTrue(result.getList().size() > 1);
  }
}