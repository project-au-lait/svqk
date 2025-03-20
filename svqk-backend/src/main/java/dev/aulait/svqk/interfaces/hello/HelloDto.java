package dev.aulait.svqk.interfaces.hello;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data // <.>
@Builder
public class HelloDto {
  @Schema(required = true, readOnly = true) // <.>
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer id;

  private String message;
}
