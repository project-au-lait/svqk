package openapi;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.smallrye.openapi.OpenApiFilter;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.PathItem;
import org.eclipse.microprofile.openapi.models.Operation;
import org.eclipse.microprofile.openapi.models.Components;
import org.eclipse.microprofile.openapi.models.parameters.Parameter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@OpenApiFilter
@RegisterForReflection
public class PathParamOrderFilter implements OASFilter {
    private static final Pattern PATH_VAR = Pattern.compile("\\{([^}/]+)\\}");

    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        if (openAPI == null || openAPI.getPaths() == null) return;
        Map<String, Parameter> componentsParams = Optional.ofNullable(openAPI.getComponents())
                .map(Components::getParameters)
                .orElse(Collections.emptyMap());

        for (Map.Entry<String, PathItem> e : openAPI.getPaths().getPathItems().entrySet()) {
            String path = e.getKey();
            List<String> order = extractPathVariables(path);
            if (order.size() < 2) continue;

            PathItem pathItem = e.getValue();
            List<Parameter> pathParams = pathItem.getParameters();
            if (pathParams != null) {
                pathItem.setParameters(reorder(pathParams, order, componentsParams));
            }

            for (Operation op : getOperations(pathItem)) {
                if (op != null && op.getParameters() != null) {
                    op.setParameters(reorder(op.getParameters(), order, componentsParams));
                }
            }
        }
    }

    private static List<String> extractPathVariables(String path) {
        List<String> vars = new ArrayList<>();
        Matcher m = PATH_VAR.matcher(path);
        while (m.find()) {
            String raw = m.group(1);
            int colon = raw.indexOf(':');
            vars.add(colon >= 0 ? raw.substring(0, colon) : raw);
        }
        return vars;
    }

    private static List<Operation> getOperations(PathItem pathItem) {
        Map<PathItem.HttpMethod, Operation> ops = pathItem.getOperations();
        if (ops != null) {
            return new ArrayList<>(ops.values());
        }
        
        return Arrays.asList(
                pathItem.getGET(),
                pathItem.getPUT(),
                pathItem.getPOST(),
                pathItem.getDELETE(),
                pathItem.getOPTIONS(),
                pathItem.getHEAD(),
                pathItem.getPATCH(),
                pathItem.getTRACE()
        );
    }

    private static List<Parameter> reorder(List<Parameter> params,
                                         List<String> order,
                                         Map<String, Parameter> components) {
        return params.stream()
                .map(p -> new ResolvedParameter(p, components, order))
                .sorted(Comparator
                        .comparing(ResolvedParameter::isPath).reversed()
                        .thenComparingInt(ResolvedParameter::getPathIndex))
                .map(ResolvedParameter::getRaw)
                .toList();
    }

    private static final class ResolvedParameter {
        private final Parameter raw;
        private final Parameter effective;
        private final boolean isPath;
        private final int pathIndex;

        ResolvedParameter(Parameter raw, Map<String, Parameter> components, List<String> order) {
            this.raw = raw;
            this.effective = deref(raw, components);
            this.isPath = Parameter.In.PATH.equals(effective.getIn());
            this.pathIndex = isPath ? findPathIndex(order, effective.getName()) : Integer.MAX_VALUE;
        }

        private static Parameter deref(Parameter p, Map<String, Parameter> components) {
            String ref = p.getRef();
            if (ref == null) return p;
            String key = ref.contains("/") ? ref.substring(ref.lastIndexOf('/') + 1) : ref;
            return components.getOrDefault(key, p);
        }

        private static int findPathIndex(List<String> order, String name) {
            if (name == null) return Integer.MAX_VALUE;
            for (int i = 0; i < order.size(); i++) {
                if (name.equalsIgnoreCase(order.get(i))) return i;
            }
            return Integer.MAX_VALUE;
        }

        Parameter getRaw() { return raw; }
        boolean isPath() { return isPath; }
        int getPathIndex() { return pathIndex; }
    }
}