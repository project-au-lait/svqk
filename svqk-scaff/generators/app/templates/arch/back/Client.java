<% include('../../lib/field-util', { fields }); -%>
package <%= interfacesPkgNm %>;

import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_PATH;
import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_GET_PATH;
import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_SEARCH_PATH;
import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmPascal %>SearchResultDto;
import static dev.aulait.svqk.arch.test.RestAssuredUtils.given;

public class <%= entityNmPascal %>Client {

  public <%= entityNmPascal %>Dto get(<%= idJavaType %> id) {
    return given()
        .get(<%= entityNmAllCaps %>_PATH + "/" + <%= entityNmAllCaps %>_GET_PATH, id)
        .then()
        .statusCode(200)
        .extract()
        .as(<%= entityNmPascal %>Dto.class);
  }

  public <%= idJavaType %> save(<%= entityNmPascal %>Dto dto) {
    return given()
        .body(dto)
        .post(<%= entityNmAllCaps %>_PATH)
        .then()
        .statusCode(200)
        .extract()
      <%_ if (idJavaType === 'Integer') { -%>
        .jsonPath()
        .getInt(".");
      <%_ } else if(idJavaType === 'String') { -%>
        .asString();
      <%_ } else { -%>
        .as(<%= idJavaType %>.class);
      <%_ } -%>
  }

  public <%= idJavaType %> update(<%= entityNmPascal %>Dto dto) {
    return given()
        .body(dto)
        .put(<%= entityNmAllCaps %>_PATH)
        .then()
        .statusCode(200)
        .extract()
      <%_ if (idJavaType === 'Integer') { -%>
        .jsonPath()
        .getInt(".");
      <%_ } else if(idJavaType === 'String') { -%>
        .asString();
      <%_ } else { -%>
        .as(<%= idJavaType %>.class);
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