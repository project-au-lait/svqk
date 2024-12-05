package dev.aulait.svqk.interfaces.hello;

import dev.aulait.svqk.domain.hello.HelloEntity;
import dev.aulait.svqk.domain.hello.HelloService;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Path(HelloController.HELLO_PATH)
@RequiredArgsConstructor
public class HelloController {

  private final HelloService helloService;

  static final String HELLO_PATH = ApiPath.ROOT + "/hello";

  static final String HELLO_GET_PATH = "{id}";

  @GET
  @Path(HELLO_GET_PATH)
  public HelloDto get(@PathParam("id") int id) {

    HelloEntity entity = helloService.find(id);

    return HelloDto.builder().id(entity.getId()).message(entity.getMessage()).build();
  }

  @POST
  public int save(@Valid HelloDto dto) {

    HelloEntity entity = new HelloEntity();
    entity.setId(dto.getId());
    entity.setMessage(dto.getMessage());

    HelloEntity savedEntity = helloService.save(entity);

    return savedEntity.getId();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  static class ApiPath {
    static final String ROOT = "/api/v1";
  }
}
