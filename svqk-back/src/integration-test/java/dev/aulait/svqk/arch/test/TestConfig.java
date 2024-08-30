package dev.aulait.svqk.arch.test;

import java.util.Optional;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

@Slf4j
@Getter
@ToString
public class TestConfig {

  private static TestConfig instance;

  private String baseUrl;

  public static TestConfig getInstance() {
    if (instance == null) {
      instance = new TestConfig();
      instance.load();
    }
    return instance;
  }

  void load() {
    Config config = ConfigProvider.getConfig();

    Optional<String> testHost = config.getOptionalValue("quarkus.http.test-host", String.class);
    Optional<Integer> testPort = config.getOptionalValue("quarkus.http.test-port", Integer.class);

    baseUrl = "http://" + testHost.orElse("localhost") + ":" + testPort.orElse(8080);

    log.info("Config: {}", this);
  }
}
