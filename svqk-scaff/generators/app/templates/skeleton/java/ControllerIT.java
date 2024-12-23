package <%= interfacesPkgNm %>;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import java.util.Random;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
class <%= entityNmPascal %>ControllerIT {

  <%= entityNmPascal %>Client client = new <%= entityNmPascal %>Client();

  @Test
  void testCrud() {
    Integer id = new Random().nextInt();

    <%= entityNmPascal %>Dto dto = <%= entityNmPascal %>Dto.builder().id(id).build();

    // Create
    Integer createdId = client.save(dto);
    assertEquals(id, createdId);

    // Reference
    <%= entityNmPascal %>Dto createdDto = client.get(id);
    assertEquals(id, createdDto.getId());

    // Update
    // delete
  }
}
