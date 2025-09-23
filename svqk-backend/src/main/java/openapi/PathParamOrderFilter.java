package openapi;

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

    openAPI.getPaths().getPathItems().entrySet().stream()
        .filter(this::hasMultiplePathVariables)
        .forEach(this::reorderPathParameters);
  }

  private boolean hasMultiplePathVariables(Map.Entry<String, PathItem> entry) {
    return extractPathVariables(entry.getKey()).size() >= 2;
  }

  private void reorderPathParameters(Map.Entry<String, PathItem> entry) {
    String path = entry.getKey();
    PathItem pathItem = entry.getValue();
    List<String> pathParamNames = extractPathVariables(path);

    pathItem
        .getOperations()
        .values()
        .forEach(
            operation -> {
              if (operation.getParameters() != null) {
                operation.setParameters(reorder(operation.getParameters(), pathParamNames));
              }
            });
  }

  private List<String> extractPathVariables(String path) {
    return PATH_VAR.matcher(path).results().map(match -> match.group(1)).toList();
  }

  private List<Parameter> reorder(List<Parameter> params, List<String> pathParamNames) {
    Map<String, Integer> indexMap = new HashMap<>();
    for (int i = 0; i < pathParamNames.size(); i++) {
      indexMap.put(pathParamNames.get(i), i);
    }

    return params.stream()
        .sorted(
            Comparator.comparingInt(
                param -> indexMap.getOrDefault(param.getName(), Integer.MAX_VALUE)))
        .toList();
  }
}
