package dev.aulait.svqk.arch.exception;

import lombok.Data;

@Data
public class ErrorResponseDto {

  private String type;
  private String title;
  private String detail;
}
