package dev.aulait.svqk.interfaces.hello;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.specification.RequestSpecification;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
class HelloControllerIT {

  @Test
  void test() {
    Config config = ConfigProvider.getConfig();
    int testPort = config.getValue("quarkus.http.test-port", Integer.class);

    RequestSpecification request = given()
        .baseUri("http://localhost:" + testPort)
        .contentType("application/json; charset=UTF-8");

    HelloDto hello = HelloDto.builder().id(new Random().nextInt()).message(RandomStringUtils.randomAscii(6)).build();

    String id = request.body(hello).post("/api/hello").then().extract().asString();
    request.get("/api/hello/" + id).then().body("message", is(hello.getMessage()));
  }

}