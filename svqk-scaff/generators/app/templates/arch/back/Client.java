<%_ include('../../lib/interface-common', { idField, compIdFields }); -%>
<%_
getMethodArgs = buildArgs((field) => `${field.javaType} ${field.fieldName}`);
givenGetArgs = buildArgs((field) => field.fieldName);
-%>
package <%= interfacesPkgNm %>;

import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.*;
import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmPascal %>SearchResultDto;
import static dev.aulait.svqk.arch.test.RestAssuredUtils.given;

public class <%= entityNmPascal %>Client {

  public <%= entityNmPascal %>Dto get(<%= getMethodArgs %>) {
    return given()
        .get(<%= entityNmAllCaps %>_PATH + "/" + <%= entityNmAllCaps %>_GET_PATH, <%= givenGetArgs %>)
        .then()
        .statusCode(200)
        .extract()
        .as(<%= entityNmPascal %>Dto.class);
  }

  public <%= interfaceIdType %> save(<%= entityNmPascal %>Dto dto) {
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
        .as(<%= interfaceIdType %>.class);
      <%_ } -%>
  }

  public <%= interfaceIdType %> update(<%= entityNmPascal %>Dto dto) {
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
        .as(<%= interfaceIdType %>.class);
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