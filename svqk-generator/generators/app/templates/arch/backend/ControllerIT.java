<% include('../../lib/interface-common'); -%>
<%_
clientIdArgs = compIdFields ? ifcom.buildArgs((field) => `id.get${field.fieldNmPascal}()`) : "id";
-%>
package <%= interfacesPkgNm %>;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.Test;

import dev.aulait.svqk.arch.exception.ErrorResponseDto;
import <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmPascal %>SearchResultDto;

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
    <%= entityNmPascal %>Dto refDto = client.get(<%= clientIdArgs %>);
    assertEquals(id, refDto.get<%= idField.fieldNmPascal %>());

    // Update
    // TODO Implementation of assembling a request and assertion
    <%= ifcom.interfaceIdType %> updatedId = client.update(<%= clientIdArgs %>, dto);

    // Search
    <%= entityNmPascal %>SearchCriteriaDto criteria = new <%= entityNmPascal %>SearchCriteriaDto();
    <%= entityNmPascal %>SearchResultDto result = client.search(criteria);
    assertTrue(result.getList().size() > 0);

    <%= entityNmPascal %>Dto updated<%= entityNmPascal %> = client.get(<%= clientIdArgs %>);

    // Delete
    <%= ifcom.interfaceIdType %> deletedId = client.delete(<%= clientIdArgs %>, updated<%= entityNmPascal %>);
    assertEquals(deletedId, id);

    ErrorResponseDto error = client.getWithError(<%= clientIdArgs %>);
    assertEquals(Status.NOT_FOUND, error.getStatus());
  }
}