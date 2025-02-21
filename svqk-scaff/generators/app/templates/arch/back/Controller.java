<%_ include('../../lib/interface-common', { idField, compIdFields }); -%>
<%_
getPath = idFields.map((field) => `{${field.fieldName}}`).join("/");
getMethodArgs = buildArgs((field) => `@PathParam("${field.fieldName}") ${field.javaType} ${field.fieldName}`);
-%>
package <%= interfacesPkgNm %>;

import dev.aulait.svqk.arch.search.SearchCriteriaVo;
import dev.aulait.svqk.arch.search.SearchResultDto;
import dev.aulait.svqk.arch.search.SearchResultVo;
import dev.aulait.svqk.arch.util.BeanUtils;
import <%= domainPkgNm %>.<%= entityNmPascal %>Entity;
<%_ if(compIdFields) { -%>
import <%= domainPkgNm %>.<%= entityNmPascal %>EntityId;
<%_ } -%>
import <%= domainPkgNm %>.<%= entityNmPascal %>Service;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.RequiredArgsConstructor;

@Path(<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_PATH)
@RequiredArgsConstructor
public class <%= entityNmPascal %>Controller {

  private final <%= entityNmPascal %>Service <%= entityNmCamel %>Service;

  private final <%= entityNmPascal %>Factory <%= entityNmCamel %>Factory;

  static final String <%= entityNmAllCaps %>_PATH = "<%= entityNmCamel %>";

  static final String <%= entityNmAllCaps %>_GET_PATH = "<%= getPath %>";

  static final String <%= entityNmAllCaps %>_SEARCH_PATH = "search";

  public static class <%= entityNmPascal %>SearchResultDto extends SearchResultDto<<%= entityNmPascal %>Dto> {} 

  @GET
  @Path(<%= entityNmAllCaps %>_GET_PATH)
  public <%= entityNmPascal %>Dto get(<%- getMethodArgs %>) {
  <%_ if (compIdFields) { -%>
    <%= entityNmPascal %>Entity entity =
        <%= entityNmCamel %>Service.find(
            <%= idField.javaType %>.builder()
              <%_ compIdFields.forEach((compIdField) => { -%>
                .<%= compIdField.fieldName %>(<%= compIdField.fieldName %>)
              <%_ }); -%>
                .build());
  <%_ } else { -%>
    <%= entityNmPascal %>Entity entity = <%= entityNmCamel %>Service.find(<%= idField.fieldName %>);
  <%_ } -%>

    return BeanUtils.map(entity, <%= entityNmPascal %>Dto.class);
  }

  @POST
  public <%= interfaceIdType %> save(@Valid <%= entityNmPascal %>Dto dto) {
    <%= entityNmPascal %>Entity entity = BeanUtils.map(dto, <%= entityNmPascal %>Entity.class);

    <%= entityNmPascal %>Entity savedEntity = <%= entityNmCamel %>Service.save(entity);

  <%_ if (compIdFields) { -%>
    return BeanUtils.map(savedEntity.get<%= idField.fieldNmPascal %>(), <%= interfaceIdType %>.class);
  <%_ } else { -%>
    return savedEntity.get<%= idField.fieldNmPascal %>();
  <%_ } -%>
  }

  @PUT
  public <%= interfaceIdType %> update(@Valid <%= entityNmPascal %>Dto dto) {
    <%= entityNmPascal %>Entity entity = BeanUtils.map(dto, <%= entityNmPascal %>Entity.class);

    <%= entityNmPascal %>Entity updatedEntity = <%= entityNmCamel %>Service.save(entity);

  <%_ if (compIdFields) { -%>
    return BeanUtils.map(updatedEntity.get<%= idField.fieldNmPascal %>(), <%= interfaceIdType %>.class);
  <%_ } else { -%>
    return updatedEntity.get<%= idField.fieldNmPascal %>();
  <%_ } -%>
  }

  @POST
  @Path(<%= entityNmAllCaps %>_SEARCH_PATH)
  public <%= entityNmPascal %>SearchResultDto search(<%= entityNmPascal %>SearchCriteriaDto dto) {
    SearchCriteriaVo vo = <%= entityNmCamel %>Factory.build(dto);
    SearchResultVo<<%= entityNmPascal %>Entity> result = <%= entityNmCamel %>Service.search(vo);

    return <%= entityNmCamel %>Factory.build(result);
  }
}
