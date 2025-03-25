<% include('../../lib/interface-common'); -%>
<%_
getPath = ifcom.idFields.map((field) => `{${field.fieldName}}`).join("/");
getMethodArgs = ifcom.buildArgs((field) => `@PathParam("${field.fieldName}") ${field.javaType} ${field.fieldName}`);
-%>
package <%= interfacesPkgNm %>;

import dev.aulait.sqb.SearchResult;
import dev.aulait.svqk.arch.util.BeanUtils;
<%= ifcom.imports %>
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.RequiredArgsConstructor;

@Path(<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_PATH)
@RequiredArgsConstructor
public class <%= entityNmPascal %>Controller {

  private final <%= entityNmPascal %>Service <%= entityNmCamel %>Service;

  static final String <%= entityNmAllCaps %>_PATH = "<%= entityNmCamel %>";

  static final String <%= entityNmAllCaps %>_GET_PATH = "<%= getPath %>";

  public static class <%= entityNmPascal %>SearchResultDto extends SearchResult<<%= entityNmPascal %>Dto> {} 

  @GET
  @Path(<%= entityNmAllCaps %>_GET_PATH)
  public <%= entityNmPascal %>Dto get(<%- getMethodArgs %>) {
<%= ifcom.buildEntity %>

    return BeanUtils.map(entity, <%= entityNmPascal %>Dto.class);
  }

  @POST
  public <%= ifcom.interfaceIdType %> save(@Valid <%= entityNmPascal %>Dto dto) {
    <%= entityNmPascal %>Entity entity = BeanUtils.map(dto, <%= entityNmPascal %>Entity.class);

    <%= entityNmPascal %>Entity savedEntity = <%= entityNmCamel %>Service.save(entity);

<%= ifcom.buildReturnString("savedEntity") %>
  }
}
