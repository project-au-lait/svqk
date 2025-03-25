package dev.aulait.svqk.interfaces.hello;

import lombok.Builder;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data // <.>
@Builder
public class HelloDto {
  @Schema(required = true) // <.>
  private Integer id;

  private String message;
}
