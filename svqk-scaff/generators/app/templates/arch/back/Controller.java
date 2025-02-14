package <%= interfacesPkgNm %>;

import dev.aulait.svqk.arch.search.SearchCriteriaVo;
import dev.aulait.svqk.arch.search.SearchResultDto;
import dev.aulait.svqk.arch.search.SearchResultVo;
import dev.aulait.svqk.arch.util.BeanUtils;
import <%= domainPkgNm %>.<%= entityNmPascal %>Entity;
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

  static final String <%= entityNmAllCaps %>_GET_PATH = "{id}";

  static final String <%= entityNmAllCaps %>_SEARCH_PATH = "search";

  public static class <%= entityNmPascal %>SearchResultDto extends SearchResultDto<<%= entityNmPascal %>Dto> {} 

  @GET
  @Path(<%= entityNmAllCaps %>_GET_PATH)
  public <%= entityNmPascal %>Dto get(@PathParam("id") <%= idField.javaType %> id) {
    <%= entityNmPascal %>Entity entity = <%= entityNmCamel %>Service.find(id);

    return <%= entityNmPascal %>Dto.builder()
    <%_ fields.forEach(function(field) { -%>
      .<%= field.fieldName %>(entity.get<%= field.fieldName.charAt(0).toUpperCase() + field.fieldName.slice(1) %>())
    <%_ }); -%>
      .build();
  }

  @POST
  public <%= idField.javaType %> save(@Valid <%= entityNmPascal %>Dto dto) {
    <%= entityNmPascal %>Entity entity = <%= entityNmPascal %>Entity.builder()
    <%_ fields.forEach(function(field) { -%>
      .<%= field.fieldName %>(dto.get<%= field.fieldName.charAt(0).toUpperCase() + field.fieldName.slice(1) %>())
    <%_ }); -%>
      .build();

    <%= entityNmPascal %>Entity savedEntity = <%= entityNmCamel %>Service.save(entity);

    return savedEntity.getId();
  }

  @PUT
  public <%= idField.javaType %> update(@Valid <%= entityNmPascal %>Dto dto) {
    <%= entityNmPascal %>Entity entity = BeanUtils.map(dto, <%= entityNmPascal %>Entity.class);

    <%= entityNmPascal %>Entity updatedEntity = <%= entityNmCamel %>Service.save(entity);

    return updatedEntity.getId();
  }

  @POST
  @Path(<%= entityNmAllCaps %>_SEARCH_PATH)
  public <%= entityNmPascal %>SearchResultDto search(<%= entityNmPascal %>SearchCriteriaDto dto) {
    SearchCriteriaVo vo = <%= entityNmCamel %>Factory.build(dto);
    SearchResultVo<<%= entityNmPascal %>Entity> result = <%= entityNmCamel %>Service.search(vo);

    return <%= entityNmCamel %>Factory.build(result);
  }
}
