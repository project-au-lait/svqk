package openapi;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.smallrye.openapi.OpenApiFilter;
import java.util.*;
import java.util.regex.Pattern;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.Operation;
import org.eclipse.microprofile.openapi.models.PathItem;
import org.eclipse.microprofile.openapi.models.parameters.Parameter;

@OpenApiFilter
@RegisterForReflection
public class PathParamOrderFilter implements OASFilter {
  private static final Pattern PATH_VAR = Pattern.compile("\\{([^}/]+)\\}");

  @Override
  public void filterOpenAPI(OpenAPI openAPI) {
    if (openAPI == null || openAPI.getPaths() == null) return;

    for (Map.Entry<String, PathItem> e : openAPI.getPaths().getPathItems().entrySet()) {
      String path = e.getKey();
      List<String> pathParamNames = extractPathVariables(path);
      if (pathParamNames.size() < 2) continue;

      PathItem pathItem = e.getValue();
      for (Operation op : pathItem.getOperations().values()) {
        if (op.getParameters() != null) {
          op.setParameters(reorder(op.getParameters(), pathParamNames));
        }
      }
    }
  }

  private static List<String> extractPathVariables(String path) {
    return PATH_VAR.matcher(path).results().map(match -> match.group(1)).toList();
  }

  private static List<Parameter> reorder(List<Parameter> params, List<String> pathParamNames) {
    Map<String, Integer> indexMap = new HashMap<>();
    for (int i = 0; i < pathParamNames.size(); i++) {
      indexMap.put(pathParamNames.get(i), i);
    }

    return params.stream()
        .sorted(Comparator.comparingInt(p -> indexMap.getOrDefault(p.getName(), Integer.MAX_VALUE)))
        .toList();
  }
}
