<% include('../../lib/interface-common'); -%>
<%_
idPath = ifcom.idFields.map((field) => `{${field.fieldName}}`).join("/");
idMethodArgs = ifcom.buildArgs((field) => `@PathParam("${field.fieldName}") ${field.javaType} ${field.fieldName}`);
operationParametersAnnotation = `@Parameters({\n  ` +
  ifcom.idFields.map((field) =>
    `@Parameter(name = "${field.fieldName}", in = ParameterIn.PATH, required = true)`
  ).join(",\n  ") + `\n})`;
-%>
package <%= interfacesPkgNm %>;

import dev.aulait.sqb.SearchCriteria;
import dev.aulait.sqb.SearchResult;
import dev.aulait.svqk.arch.util.BeanUtils;
<%= ifcom.imports %>
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;

@Path(<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_PATH)
@RequiredArgsConstructor
public class <%= entityNmPascal %>Controller {

  private final <%= entityNmPascal %>Service <%= entityNmCamel %>Service;
  private final <%= entityNmPascal %>Factory <%= entityNmCamel %>Factory;

  static final String <%= entityNmAllCaps %>_PATH = "<%= entityNmCamel %>";
  static final String <%= entityNmAllCaps %>_ID_PATH = "<%= idPath %>";
  static final String <%= entityNmAllCaps %>_SEARCH_PATH = "search";

  public static class <%= entityNmPascal %>SearchResultDto extends SearchResult<<%= entityNmPascal %>Dto> {}

  @GET
  @Path(<%= entityNmAllCaps %>_ID_PATH)
  <%- operationParametersAnnotation %>
  public <%= entityNmPascal %>Dto get(<%- idMethodArgs %>) {
<%= ifcom.buildEntity %>

    return BeanUtils.map(entity, <%= entityNmPascal %>Dto.class);
  }

  @POST
  public <%= ifcom.interfaceIdType %> save(@Valid <%= entityNmPascal %>Dto dto) {
    <%= entityNmPascal %>Entity entity = BeanUtils.map(dto, <%= entityNmPascal %>Entity.class);

    <%= entityNmPascal %>Entity savedEntity = <%= entityNmCamel %>Service.save(entity);

<%= ifcom.buildReturnString("savedEntity") %>
  }

  @PUT
  @Path(<%= entityNmAllCaps %>_ID_PATH)
  <%- operationParametersAnnotation %>
  public <%= ifcom.interfaceIdType %> update(<%- idMethodArgs %>, @Valid <%= entityNmPascal %>Dto dto) {
    <%= entityNmPascal %>Entity entity = BeanUtils.map(dto, <%= entityNmPascal %>Entity.class);

<%= ifcom.buildEntityId %>

    <%= entityNmPascal %>Entity updatedEntity = <%= entityNmCamel %>Service.save(entity);
<%= ifcom.buildReturnString("updatedEntity") %>
  }

  @DELETE
  @Path(<%= entityNmAllCaps %>_ID_PATH)
  <%- operationParametersAnnotation %>
  public <%= ifcom.interfaceIdType %> delete(<%- idMethodArgs %>, @Valid <%= entityNmPascal %>Dto dto) {
    <%= entityNmPascal %>Entity entity = BeanUtils.map(dto, <%= entityNmPascal %>Entity.class);

<%= ifcom.buildEntityId %>

    <%= entityNmCamel %>Service.delete(entity);
<%= ifcom.buildReturnString("entity") %>
  }

  @POST
  @Path(<%= entityNmAllCaps %>_SEARCH_PATH)
  public <%= entityNmPascal %>SearchResultDto search(<%= entityNmPascal %>SearchCriteriaDto dto) {
    SearchCriteria searchCriteria = <%= entityNmCamel %>Factory.build(dto);
    SearchResult<<%= entityNmPascal %>Entity> result = <%= entityNmCamel %>Service.search(searchCriteria);

    return <%= entityNmCamel %>Factory.build(result);
  }
}
