package dev.aulait.svqk.arch.jpa;

import static org.hibernate.jpa.SpecHints.HINT_SPEC_FETCH_GRAPH;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JpaUtils {

  public static <T> EntityGraph<T> buildEntityGraph(
      EntityManager em, Class<T> entityClass, List<String> attributeNodes) {

    EntityGraph<T> entityGraph = em.createEntityGraph(entityClass);
    entityGraph.addAttributeNodes(attributeNodes.toArray(String[]::new));

    return entityGraph;
  }

  public static <T> Map<String, Object> buildFetchGraph(EntityManager em, Class<T> entityClass) {
    return buildFetchGraph(em, entityClass, getAllJoinColumnFields(entityClass));
  }

  public static <T> Map<String, Object> buildFetchGraph(
      EntityManager em, Class<T> entityClass, List<String> fetchFields) {

    return Map.of(HINT_SPEC_FETCH_GRAPH, buildEntityGraph(em, entityClass, fetchFields));
  }

  private static <T> List<String> getAllJoinColumnFields(Class<T> entityClass) {
    return Stream.of(entityClass.getDeclaredFields())
        .filter(f -> Objects.nonNull(f.getAnnotation(JoinColumn.class)))
        .map(Field::getName)
        .toList();
  }
}
