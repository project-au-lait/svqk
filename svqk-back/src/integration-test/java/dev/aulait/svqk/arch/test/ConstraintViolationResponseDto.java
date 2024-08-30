package dev.aulait.svqk.arch.test;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ConstraintViolationResponseDto {
  private String title;
  private int status;
  private List<ConstraintViolationDto> violations = new ArrayList<>();
}
