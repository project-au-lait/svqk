package dev.aulait.svqk.arch.test;

import lombok.Data;

@Data
public class ConstraintViolationDto {
  private String field;
  private String message;
}
