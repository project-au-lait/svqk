package dev.aulait.svqk.arch.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.DestinationSetter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtils {

  private static final ModelMapper MAPPER = buildDefaultMapper();

  /**
   * Maps the source object to an instance of the specified destination type.
   *
   * @param <T> the type of the destination object
   * @param src the source object to be mapped
   * @param dstType the class of the destination type
   * @return an instance of the destination type with the mapped values from the source object
   */
  public static <T> T map(Object src, Class<T> dstType) {
    return MAPPER.map(src, dstType);
  }

  /**
   * Maps the source object to an instance of the destination type using the provided mapping
   * configuration.
   *
   * @param <D> the type of the destination object
   * @param config the mapping configuration containing the mapper
   * @param src the source object to be mapped
   * @param dstType the class of the destination type
   * @return an instance of the destination type with the mapped values from the source object
   */
  public static <D> D map(MappingConfig<?, ?> config, Object src, Class<D> dstType) {
    return config.mapper.map(src, dstType);
  }

  /**
   * Maps a collection of source objects to a list of destination objects of the specified type.
   *
   * @param <S> the type of the source objects
   * @param <D> the type of the destination objects
   * @param srcs the collection of source objects to be mapped
   * @param dstType the class of the destination type
   * @return a list of mapped destination objects, or an empty list if the source collection is null
   */
  public static <S, D> List<D> mapAll(Collection<S> srcs, Class<D> dstType) {
    if (srcs == null) {
      return Collections.emptyList();
    }
    return srcs.stream().map(src -> map(src, dstType)).toList();
  }

  /**
   * Maps a collection of source objects to a list of destination objects using the provided mapping
   * configuration.
   *
   * @param <S> the type of the source objects
   * @param <D> the type of the destination objects
   * @param config the mapping configuration to use for mapping each source object to a destination
   *     object
   * @param srcs the collection of source objects to be mapped
   * @param dstType the class type of the destination objects
   * @return a list of mapped destination objects, or an empty list if the source collection is null
   */
  public static <S, D> List<D> mapAll(
      MappingConfig<S, D> config, Collection<S> srcs, Class<D> dstType) {
    if (srcs == null) {
      return Collections.emptyList();
    }
    return srcs.stream().map(src -> map(config, src, dstType)).toList();
  }

  /**
   * Builds a mapping configuration for the specified source and destination types.
   *
   * @param <S> the type of the source object
   * @param <D> the type of the destination object
   * @param srcType the class of the source type
   * @param dstType the class of the destination type
   * @return a MappingConfig object containing the mapper and type map for the specified types
   */
  public static <S, D> MappingConfigBuilder<S, D> buildConfig(Class<S> srcType, Class<D> dstType) {
    MappingConfig<S, D> config = new MappingConfig<>();
    config.mapper = buildDefaultMapper();
    config.typeMap = config.mapper.emptyTypeMap(srcType, dstType);
    return config;
  }

  private static ModelMapper buildDefaultMapper() {
    return new ModelMapper();
  }

  public static interface MappingConfigBuilder<S, D> {

    /**
     * Skips the mapping for the specified destination property.
     *
     * @param <V> the type of the destination property
     * @param setter a function that sets the destination property
     * @return the current instance of MappingConfigBuilder for method chaining
     */
    <V> MappingConfigBuilder<S, D> skip(DestinationSetter<D, V> setter);

    /**
     * Finalizes the mapping configuration by applying implicit mappings.
     *
     * @return the current MappingConfig instance
     */
    MappingConfig<S, D> build();
  }

  /**
   * A configuration class for mapping between source and destination types using ModelMapper.
   *
   * @param <S> the source type
   * @param <D> the destination type
   */
  public static class MappingConfig<S, D> implements MappingConfigBuilder<S, D> {
    private ModelMapper mapper;
    private TypeMap<S, D> typeMap;

    public <V> MappingConfigBuilder<S, D> skip(DestinationSetter<D, V> setter) {
      typeMap.addMappings(
          m -> {
            m.skip(setter);
          });
      return this;
    }

    @Override
    public MappingConfig<S, D> build() {
      typeMap.implicitMappings();
      return this;
    }
  }
}
