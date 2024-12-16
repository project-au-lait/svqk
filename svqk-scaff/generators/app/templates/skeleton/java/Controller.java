package <%= interfacesPkgNm %>;

import <%= domainPkgNm %>.<%= entityNmPascal %>Entity;
import <%= domainPkgNm %>.<%= entityNmPascal %>Service;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Path(<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_PATH)
@RequiredArgsConstructor
public class <%= entityNmPascal %>Controller {

  private final <%= entityNmPascal %>Service <%= entityNmCamel %>Service;

  static final String <%= entityNmAllCaps %>_PATH = "/<%= entityNmCamel %>";

  static final String <%= entityNmAllCaps %>_GET_PATH = "{id}";

  @GET
  @Path(<%= entityNmAllCaps %>_GET_PATH)
  public <%= entityNmPascal %>Dto get(@PathParam("id") int id) {

    <%= entityNmPascal %>Entity entity = <%= entityNmCamel %>Service.find(id);

    return <%= entityNmPascal %>Dto.builder()
    <% fields.forEach(function(field) { %>
      .<%= field.fieldName %>(entity.get<%= field.fieldName.charAt(0).toUpperCase() + field.fieldName.slice(1) %>())
    <% }); %>.build();

  }

  @POST
  public int save(@Valid <%= entityNmPascal %>Dto dto) {

    <%= entityNmPascal %>Entity entity = <%= entityNmPascal %>Entity.builder()
    <% fields.forEach(function(field) { %>
      .<%= field.fieldName %>(dto.get<%= field.fieldName.charAt(0).toUpperCase() + field.fieldName.slice(1) %>())
    <% }); %>.build();

    <%= entityNmPascal %>Entity savedEntity = <%= entityNmCamel %>Service.save(entity);

    return savedEntity.getId();
  }
}
