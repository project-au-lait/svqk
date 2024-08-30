package dev.aulait.svqk.interfaces.hello;

import dev.aulait.svqk.domain.hello.HelloEntity;
import dev.aulait.svqk.domain.hello.HelloService;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.RequiredArgsConstructor;

@Path(HelloController.HELLO_PATH) // <.>
@RequiredArgsConstructor // <.>
public class HelloController {

  private final HelloService helloService; // <.>

  @SuppressWarnings("java:S1075")
  static final String HELLO_PATH = "/api/v1/hello";

  @SuppressWarnings("java:S1075")
  static final String HELLO_GET_PATH = "/{id}";

  @GET
  @Path(HELLO_GET_PATH)
  public HelloDto get(@PathParam("id") int id) { // <4>

    HelloEntity entity = helloService.find(id);

    return HelloDto.builder().id(entity.getId()).message(entity.getMessage()).build();
  }

  @POST
  public int save(@Valid HelloDto dto) { // <4>

    HelloEntity entity = new HelloEntity();
    entity.setId(dto.getId());
    entity.setMessage(dto.getMessage());

    HelloEntity savedEntity = helloService.save(entity);

    return savedEntity.getId();
  }
}