package dev.aulait.svqk.arch.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import dev.aulait.svqk.arch.util.BeanUtils.MappingConfig;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

class BeanUtilsTests {

  @Test
  void configMapTest() {
    MappingConfig<Model, Model> config =
        BeanUtils.buildConfig(Model.class, Model.class).skip(Model::setName).build();

    Model src = Model.builder().id("id").name("name").build();

    Model dst = BeanUtils.map(config, src, Model.class);

    assertEquals(src.getId(), dst.getId());
    assertNull(dst.getName());

    List<Model> dsts = BeanUtils.mapAll(config, List.of(src), Model.class);

    assertEquals(src.getId(), dsts.get(0).getId());
    assertNull(dsts.get(0).getName());
  }

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor
  static class Model {
    String id;
    String name;
  }
}
