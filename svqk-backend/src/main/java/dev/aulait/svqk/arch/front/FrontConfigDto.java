package dev.aulait.svqk.arch.front;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import lombok.Data;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Data
@ApplicationScoped
public class FrontConfigDto {

  @ConfigProperty(name = "app.front.PUBLIC_BACKEND_URL")
  @JsonProperty("PUBLIC_BACKEND_URL")
  private Optional<String> publicBackEndUrl;
}
