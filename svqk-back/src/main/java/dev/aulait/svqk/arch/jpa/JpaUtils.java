package dev.aulait.svqk.arch.jpa;

import static org.hibernate.jpa.SpecHints.HINT_SPEC_FETCH_GRAPH;

import dev.aulait.svqk.arch.exception.ResourceNotFoundException;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.jpa.repository.JpaRepository;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JpaUtils {

  public static <T, I> T findByIdAsResource(JpaRepository<T, I> repository, I id) {
    return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
  }

  public static <T> T findWithFetch(
      EntityManager em, Class<T> entityType, Object entityId, String... fetchFields) {

    if (ArrayUtils.isEmpty(fetchFields)) {
      fetchFields = getAllJoinColumnFields(entityType);
    }

    return Optional.ofNullable(
            em.find(
                entityType,
                entityId,
                Map.of(HINT_SPEC_FETCH_GRAPH, buildEntityGraph(em, entityType, fetchFields))))
        .orElseThrow(() -> new ResourceNotFoundException(entityId));
  }

  public static <T> EntityGraph<T> buildEntityGraph(
      EntityManager em, Class<T> entityType, String[] attributeNodes) {

    EntityGraph<T> entityGraph = em.createEntityGraph(entityType);
    entityGraph.addAttributeNodes(attributeNodes);

    return entityGraph;
  }

  private static <T> String[] getAllJoinColumnFields(Class<T> entityType) {
    return Stream.of(entityType.getDeclaredFields())
        .filter(f -> Objects.nonNull(f.getAnnotation(JoinColumn.class)))
        .map(Field::getName)
        .toArray(String[]::new);
  }
}
