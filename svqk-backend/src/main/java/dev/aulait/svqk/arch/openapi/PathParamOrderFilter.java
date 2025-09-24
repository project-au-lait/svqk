package dev.aulait.svqk.arch.openapi;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.smallrye.openapi.OpenApiFilter;
import java.util.*;
import java.util.regex.Pattern;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.PathItem;
import org.eclipse.microprofile.openapi.models.parameters.Parameter;

@OpenApiFilter
@RegisterForReflection
public class PathParamOrderFilter implements OASFilter {
  private static final Pattern PATH_VAR = Pattern.compile("\\{([^}/]+)\\}");

  @Override
  public void filterOpenAPI(OpenAPI openAPI) {
    if (openAPI == null || openAPI.getPaths() == null) return;

    openAPI.getPaths().getPathItems().entrySet().stream().forEach(this::reorderPathParameters);
  }

  private void reorderPathParameters(Map.Entry<String, PathItem> entry) {
    List<String> pathParamNames = extractPathParamNames(entry.getKey());
    if (pathParamNames.size() < 2) {
      return;
    }

    entry
        .getValue()
        .getOperations()
        .values()
        .forEach(
            op -> {
              List<Parameter> reordered = orderByPathParamNames(op.getParameters(), pathParamNames);
              op.setParameters(reordered);
            });
  }

  private List<String> extractPathParamNames(String path) {
    return PATH_VAR.matcher(path).results().map(match -> match.group(1)).toList();
  }

  private List<Parameter> orderByPathParamNames(
      List<Parameter> params, List<String> pathParamNames) {
    Map<String, Integer> indexMap = new HashMap<>();
    for (int i = 0; i < pathParamNames.size(); i++) {
      indexMap.put(pathParamNames.get(i), i);
    }

    return params.stream()
        .sorted(
            Comparator.comparingInt(
                param -> indexMap.getOrDefault(param.getName(), Integer.MAX_VALUE))) // (1)
        .toList();
  }

  // (1) Reason for MAX_VALUE fallback:
  // The order of non-path parameters is preserved. This is done by assigning
  // Integer.MAX_VALUE to parameters not found in pathParamNames so they are always
  // placed at the end. When multiple non-path parameters share this value, Java's
  // stable sort ensures their original relative order is maintained. (a)
  //
  // References:
  // (a)
  // https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Collections.html#sort(java.util.List)
}
