<%_ include('../../lib/interface-common', { idField, compIdFields }); -%>
<%_
getMethodArgs = buildArgs((field) => `${field.javaType} ${field.fieldName}`);
givenGetArgs = buildArgs((field) => field.fieldName);
-%>
package <%= interfacesPkgNm %>;

import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.*;
import static <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmPascal %>SearchResultDto;
import dev.aulait.svqk.arch.test.RestClientUtils;

public class <%= entityNmPascal %>Client {

  public <%= entityNmPascal %>Dto get(<%= getMethodArgs %>) {
    return RestClientUtils.get(<%= entityNmAllCaps %>_PATH + "/" + <%= entityNmAllCaps %>_GET_PATH, <%= entityNmPascal %>Dto.class, <%= givenGetArgs %>);
  }

  public <%= interfaceIdType %> save(<%= entityNmPascal %>Dto dto) {
    return RestClientUtils.post(<%= entityNmAllCaps %>_PATH, dto, <%= interfaceIdType %>.class);
  }

  public <%= interfaceIdType %> update(<%= entityNmPascal %>Dto dto) {
    return RestClientUtils.put(<%= entityNmAllCaps %>_PATH, dto, <%= interfaceIdType %>.class);
  }

  public <%= entityNmPascal %>SearchResultDto search(<%= entityNmPascal %>SearchCriteriaDto dto) {
    return RestClientUtils.post(<%= entityNmAllCaps %>_PATH + "/" + <%= entityNmAllCaps %>_SEARCH_PATH, dto, <%= entityNmPascal %>SearchResultDto.class);
  }
}