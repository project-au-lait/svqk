package <%= interfacesPkgNm %>;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * This integration test is automatically generated.
 * 
 * The test generated is a sample that only checks the id.
 * Tests will fail depending on the entity configuration.
 * (E.g. required fields other than id are present, etc.
 * 
 * Change the content of the test if necessary.
 */
@QuarkusIntegrationTest
class <%= entityNmPascal %>ControllerIT {

  <%= entityNmPascal %>Client client = new <%= entityNmPascal %>Client();

  @Test
  void testCrud() {
    int id = new Random().nextInt();
    <%= entityNmPascal %>Dto dto = <%= entityNmPascal %>Dto.builder().id(id).build();

    // Create
    int createdId = client.save(dto);
    assertEquals(id, createdId);

    // Reference
    <%= entityNmPascal %>Dto refDto = client.get(id);
    assertEquals(id, refDto.getId());
  }
}
