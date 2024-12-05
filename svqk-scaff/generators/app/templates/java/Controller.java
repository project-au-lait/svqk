package <%= interfacesPkgName %>;

import <%= domainPkgName %>.<%= entNamePascal %>Entity;
import <%= domainPkgName %>.<%= entNamePascal %>Service;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Path(<%= entNamePascal %>Controller.<%= entNameAllCaps %>_PATH)
@RequiredArgsConstructor
public class <%= entNamePascal %>Controller {

  private final <%= entNamePascal %>Service <%= entNameCamel %>Service;

  static final String <%= entNameAllCaps %>_PATH = ApiPath.ROOT + "/<%= entNameCamel %>";

  static final String <%= entNameAllCaps %>_GET_PATH = "{id}";

  @GET
  @Path(<%= entNameAllCaps %>_GET_PATH)
  public <%= entNamePascal %>Dto get(@PathParam("id") int id) {

    <%= entNamePascal %>Entity entity = <%= entNameCamel %>Service.find(id);

    return <%= entNamePascal %>Dto.builder().id(entity.getId()).message(entity.getMessage()).build();
  }

  @POST
  public int save(@Valid <%= entNamePascal %>Dto dto) {

    <%= entNamePascal %>Entity entity = new <%= entNamePascal %>Entity();
    entity.setId(dto.getId());
    entity.setMessage(dto.getMessage());

    <%= entNamePascal %>Entity savedEntity = <%= entNameCamel %>Service.save(entity);

    return savedEntity.getId();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  static class ApiPath {
    static final String ROOT = "/api/v1";
  }
}
