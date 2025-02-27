package dev.aulait.svqk.arch.exception;

import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class OptimisticLockExceptionMapper implements ExceptionMapper<OptimisticLockException> {

  @Override
  public Response toResponse(OptimisticLockException exception) {
    ErrorResponseDto response = new ErrorResponseDto();

    response.setTitle("Optimistic Lock Error");
    response.setDetail("The data you tried to update may have been updated by someone else");

    return Response.status(Status.CONFLICT).entity(response).build();

  }

}
