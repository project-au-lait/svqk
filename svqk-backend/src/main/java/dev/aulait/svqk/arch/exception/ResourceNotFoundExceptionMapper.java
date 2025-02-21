package dev.aulait.svqk.arch.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

  @Override
  public Response toResponse(ResourceNotFoundException exception) {
    ErrorResponseDto response = new ErrorResponseDto();

    response.setTitle("The requested resource was not found.");
    response.setDetail("The resource of id: " + exception.getId() + " does not exist.");

    return Response.status(Status.NOT_FOUND).entity(response).build();
  }
}
