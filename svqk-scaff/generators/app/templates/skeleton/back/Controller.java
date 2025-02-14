<%_
getPath = compositePk
  ? compositePk.fields.map((f) => `{${f.fieldName}}`).join("/")
  : `{${idField.fieldName}}`;

buildGetMethodArg = (field) => `@PathParam("${field.fieldName}") ${field.javaType} ${field.fieldName}`;
getMethodArgs = (compositePk ? compositePk.fields : [idField]).map(buildGetMethodArg).join(', ');
-%>
package <%= interfacesPkgNm %>;

import <%= domainPkgNm %>.<%= entityNmPascal %>Entity;
<%_ if(compositePk) { -%>
import <%= domainPkgNm %>.<%= entityNmPascal %>EntityId;
<%_ } -%>
import <%= domainPkgNm %>.<%= entityNmPascal %>Service;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.RequiredArgsConstructor;

@Path(<%= entityNmPascal %>Controller.<%= entityNmAllCaps %>_PATH)
@RequiredArgsConstructor
public class <%= entityNmPascal %>Controller {

  private final <%= entityNmPascal %>Service <%= entityNmCamel %>Service;

  static final String <%= entityNmAllCaps %>_PATH = "<%= entityNmCamel %>";

  static final String <%= entityNmAllCaps %>_GET_PATH = "<%= getPath %>";

  @GET
  @Path(<%= entityNmAllCaps %>_GET_PATH)
  public <%= entityNmPascal %>Dto get(<%- getMethodArgs %>) {
  <%_ if (compositePk) { -%>
    <%= entityNmPascal %>Entity entity =
        <%= entityNmCamel %>Service.find(
            <%= idJavaType %>.builder()
              <%_ compositePk.fields.forEach(function(pkField) { -%>
                .<%= pkField.fieldName %>(<%= pkField.fieldName %>)
              <%_ }); -%>
                .build());
  <%_ } else { -%>
    <%= entityNmPascal %>Entity entity = <%= entityNmCamel %>Service.find(idField.fieldName);
  <%_ } -%>

    return <%= entityNmPascal %>Dto.builder()
    <%_ fields.forEach(function(field) { -%>
      <%_ if (compositePk && field.id) { -%>
        <%_ compositePk.fields.forEach(function(pkField) { -%>
        .<%= pkField.fieldName %>(entity.get<%= idField.fieldNmPascal %>().get<%= pkField.fieldNmPascal %>())
        <%_ }); -%>
      <%_ } else { -%>
        .<%= field.fieldName %>(entity.get<%= field.fieldNmPascal %>())
      <%_ } -%>
    <%_ }); -%>
        .build();
  }

  @POST
  public <%= idJavaType %> save(@Valid <%= entityNmPascal %>Dto dto) {
    <%= entityNmPascal %>Entity entity = <%= entityNmPascal %>Entity.builder()
    <%_ fields.forEach(function(field) { -%>
      <%_ if (compositePk && field.id) { -%>
        .<%= idField.fieldName %>(<%= idJavaType %>.builder()
          <%_ compositePk.fields.forEach(function(pkField) { -%>
            .<%= pkField.fieldName %>(dto.get<%= pkField.fieldNmPascal %>())
          <%_ }); -%>
            .build())
      <%_ } else { -%>
        .<%= field.fieldName %>(dto.get<%= field.fieldNmPascal %>())
      <%_ } -%>
    <%_ }); -%>
        .build();

    <%= entityNmPascal %>Entity savedEntity = <%= entityNmCamel %>Service.save(entity);

    return savedEntity.getId();
  }
}
