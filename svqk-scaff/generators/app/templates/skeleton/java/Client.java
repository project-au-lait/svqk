package <%= interfacesPkgNm %>;

import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.*;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import java.util.Locale;
import java.util.Optional;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

public class <%= entityNmPascal %>Client {

  public <%= entityNmPascal %>Dto get(Integer id) {
    return given()
        .get(<%= entityNmAllCaps %>_PATH + "/" + <%= entityNmAllCaps %>_GET_PATH, id)
        .then()
        .statusCode(200)
        .extract()
        .as(<%= entityNmPascal %>Dto.class);
  }

  public Integer save(<%= entityNmPascal %>Dto dto) {
    return Integer.parseInt(
        given()
            .body(dto)
            .post(<%= entityNmAllCaps %>_PATH)
            .then()
            .statusCode(200)
            .extract()
            .asString());
  }

  private static RequestSpecification given() {
    Config config = ConfigProvider.getConfig();

    Optional<String> testHost = config.getOptionalValue("quarkus.http.test-host", String.class);
    Optional<Integer> testPort = config.getOptionalValue("quarkus.http.test-port", Integer.class);
    Optional<String> restPath = config.getOptionalValue("quarkus.rest.path", String.class);

    return io.restassured.RestAssured.given()
        .baseUri(
            "http://"
                + testHost.orElse("localhost")
                + ":"
                + testPort.orElse(8080)
                + restPath.orElse(""))
        .contentType("application/json; charset=UTF-8")
        .header("Accept-Language", Locale.getDefault().toString().replace("_", "-"))
        .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
  }
}
