package <%= interfacesPkgNm %>;

import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_PATH;
import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_GET_PATH;
import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_SEARCH_PATH;
import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmPascal %>SearchResultDto;
import static dev.aulait.svqk.arch.test.RestAssuredUtils.given;

public class <%= entityNmPascal %>Client {

  public <%= entityNmPascal %>Dto get(<%= idField.javaType %> id) {
    return given()
        .get(<%= entityNmAllCaps %>_PATH + "/" + <%= entityNmAllCaps %>_GET_PATH, id)
        .then()
        .statusCode(200)
        .extract()
        .as(<%= entityNmPascal %>Dto.class);
  }

  public <%= idField.javaType %> save(<%= entityNmPascal %>Dto dto) {
    return given()
        .body(dto)
        .post(<%= entityNmAllCaps %>_PATH)
        .then()
        .statusCode(200)
        .extract()
      <%_ if (idField.javaType === 'Integer') { -%>
        .jsonPath()
        .getInt(".");
      <%_ } else if(idField.javaType === 'String') { -%>
        .asString();
      <%_ } else { -%>
        .as(<%= idField.javaType %>.class);
      <%_ } -%>
  }

  public <%= idField.javaType %> update(<%= entityNmPascal %>Dto dto) {
    return given()
        .body(dto)
        .put(<%= entityNmAllCaps %>_PATH)
        .then()
        .statusCode(200)
        .extract()
      <%_ if (idField.javaType === 'Integer') { -%>
        .jsonPath()
        .getInt(".");
      <%_ } else if(idField.javaType === 'String') { -%>
        .asString();
      <%_ } else { -%>
        .as(<%= idField.javaType %>.class);
      <%_ } -%>
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