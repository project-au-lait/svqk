package dev.aulait.svqk.interfaces.hello;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HelloDto {
  @NotNull
  private Integer id;
  private String message;
}
