package dev.aulait.svqk.arch.exception;

import jakarta.ws.rs.core.Response.Status;
import lombok.Data;

@Data
public class ErrorResponseDto {

  private Status status;
  private String type;
  private String title;
  private String detail;
}
