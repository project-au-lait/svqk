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
public class SvqkOASFilter implements OASFilter {
  private static final Pattern PATH_VAR = Pattern.compile("\\{([^}/]+)\\}");

  @Override
  public void filterOpenAPI(OpenAPI openAPI) {
    if (openAPI == null || openAPI.getPaths() == null) return;

    openAPI.getPaths().getPathItems().entrySet().stream().forEach(this::reorderPathParameters);
  }

  /**
   * Reorders the path parameters in an OpenAPI object to match the order of placeholders defined in
   * the API path. Non-path parameters such as query and header parameters retain their original
   * relative order and are placed after the path parameters.
   *
   * <p><b>Background:</b> This reordering is performed to align with how svqk-generator specifies
   * the order of path parameters in the generated OpenAPI document (a). Without this reordering,
   * the OpenAPI document would list path parameters in alphanumeric order by their names.
   * svqk-generator generates an API Client (b) based on this OpenAPI document. In addition, when
   * svqk-generator generates code for single-record GET, UPDATE, or DELETE API calls, it arranges
   * the path parameters in the order of the primary keys of the target table (c). This order
   * corresponds to the order of placeholders defined in the API path. To keep these two parameter
   * orders consistent, this method performs the reordering.
   *
   * <p><b>(a)</b> svqk-generator uses Quarkus to generate the OpenAPI document. See <code>
   * svqk-generator/.yo-rc.json</code> (genOpenApiJsonCmd).
   *
   * <p><b>(b)</b> svqk-generator uses <code>swagger-typescript-api</code> to generate the API
   * Client. See <code>svqk-generator/src/main/lib/ApiClientGenerator.ts</code>.
   *
   * <p><b>(c)</b> This generation logic is implemented in the update-page template and the common
   * utility functions it uses:
   *
   * <ul>
   *   <li><code>generators/app/templates/arch/frontend/routes/entityId/+page.ts</code>
   *   <li><code>generators/app/templates/lib/typescript-common.ejs</code> (buildDetailApiCall)
   * </ul>
   *
   * <p><b>Problem example:</b> Without this reordering, the arguments corresponding to path
   * parameters in the API Client function are generated in alphanumeric order, which may cause
   * mismatched parameter bindings. In the example below, the caller mistakenly passes <code>
   * accountId</code> to <code>userId</code> and vice versa.
   *
   * <pre>
   * // Function in API Client
   * get: (
   *   accountId: string,
   *   userId: string,
   *   params: RequestParams = {}
   * ) =>
   *   this.request&lt;UserModel, any&gt;({
   *     path: `/api/user/${userId}/${accountId}`,
   *     method: "GET",
   *     format: "json",
   *     ...params,
   *   }),
   *
   * // Calling side
   * const user = (await ApiHandler.handle&lt;UserModel&gt;(
   *   fetch,
   *   (api) =>
   *     api.user.get(
   *       params.userId,
   *       params.accountId
   *     )
   * ));
   * </pre>
   *
   * @param entry An entry whose key is the API path and value is a PathItem. A PathItem contains
   *     Operations for each HTTP method corresponding to the path. Each Operation includes a list
   *     of Parameter objects such as path parameters. The order of this Parameter list corresponds
   *     to the parameter order in the generated OpenAPI document.
   */
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
            Comparator.comparingInt(param -> indexMap.getOrDefault(param.getName(), params.size())))
        .toList();
  }
}
