package <%= interfacesPkgNm %>;

import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_PATH;
import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_GET_PATH;
import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_SEARCH_PATH;
import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmPascal %>SearchResultDto;
import static dev.aulait.svqk.arch.test.RestAssuredUtils.given;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import java.util.Locale;
import java.util.Optional;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

public class <%= entityNmPascal %>Client {

  public <%= entityNmPascal %>Dto get(int id) {
    return given()
        .get(<%= entityNmAllCaps %>_PATH + "/" + <%= entityNmAllCaps %>_GET_PATH, id)
        .then()
        .statusCode(200)
        .extract()
        .as(<%= entityNmPascal %>Dto.class);
  }

  public int save(<%= entityNmPascal %>Dto dto) {
    return Integer.parseInt(
        given()
            .body(dto)
            .post(<%= entityNmAllCaps %>_PATH)
            .then()
            .statusCode(200)
            .extract()
            .asString());
  }

  public int update(<%= entityNmPascal %>Dto dto) {
    return Integer.parseInt(
        given()
            .body(dto)
            .put(<%= entityNmAllCaps %>_PATH)
            .then()
            .statusCode(200)
            .extract()
            .asString());
  }

  public <%= entityNmPascal %>SearchResultDto search(<%= entityNmPascal %>SearchCriteriaDto dto) {
    return given()
        .body(dto)
        .post(<%= entityNmAllCaps %>_PATH + "/" + <%= entityNmAllCaps %>_SEARCH_PATH)
        .then()
        .statusCode(200)
        .extract()
        .as(<%= entityNmPascal %>SearchResultDto.class);
  }
}