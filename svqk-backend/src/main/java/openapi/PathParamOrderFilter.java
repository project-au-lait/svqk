package openapi;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.smallrye.openapi.OpenApiFilter;
import java.util.*;
import java.util.regex.Matcher;
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
      List<String> order = extractPathVariables(path);
      if (order.size() < 2) continue;

      PathItem pathItem = e.getValue();
      for (Operation op : pathItem.getOperations().values()) {
        if (op.getParameters() != null) {
          op.setParameters(reorder(op.getParameters(), order));
        }
      }
    }
  }

  private static List<String> extractPathVariables(String path) {
    List<String> vars = new ArrayList<>();
    Matcher m = PATH_VAR.matcher(path);
    while (m.find()) {
      vars.add(m.group(1));
    }
    return vars;
  }

  private static List<Parameter> reorder(List<Parameter> params, List<String> order) {
    return params.stream()
        .map(p -> new ResolvedParameter(p, order))
        .sorted(Comparator.comparingInt(ResolvedParameter::getPathIndex))
        .map(ResolvedParameter::getRaw)
        .toList();
  }

  private static final class ResolvedParameter {
    private final Parameter raw;
    private final int pathIndex;

    ResolvedParameter(Parameter raw, List<String> order) {
      this.raw = raw;
      this.pathIndex = findPathIndex(order, raw.getName());
    }

    private static int findPathIndex(List<String> order, String name) {
      if (name == null) return Integer.MAX_VALUE;
      int idx = order.indexOf(name);
      return idx >= 0 ? idx : Integer.MAX_VALUE;
    }

    Parameter getRaw() {
      return raw;
    }

    int getPathIndex() {
      return pathIndex;
    }
  }
}
