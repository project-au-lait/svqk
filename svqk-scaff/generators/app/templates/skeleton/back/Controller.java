<%_
pkFields = compositePk ? compositePk.fields : [idField];

getPath = pkFields.map((field) => `{${field.fieldName}}`).join("/");

buildGetMethodArg = (field) => `@PathParam("${field.fieldName}") ${field.javaType} ${field.fieldName}`;
getMethodArgs = pkFields.map(buildGetMethodArg).join(', ');

idJavaType = compositePk ? `${entityNmPascal}IdDto` : idField.javaType;
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
            <%= idField.javaType %>.builder()
              <%_ compositePk.fields.forEach((pkField) => { -%>
                .<%= pkField.fieldName %>(<%= pkField.fieldName %>)
              <%_ }); -%>
                .build());
  <%_ } else { -%>
    <%= entityNmPascal %>Entity entity = <%= entityNmCamel %>Service.find(<%= idField.fieldName %>);
  <%_ } -%>

    return <%= entityNmPascal %>Dto.builder()
    <%_ fields.forEach((field) => { -%>
      <%_ if (compositePk && field.id) { -%>
        .<%= field.fieldName %>(<%= idJavaType %>.builder()
          <%_ compositePk.fields.forEach((pkField) => { -%>
            .<%= pkField.fieldName %>(entity.get<%= idField.fieldNmPascal %>().get<%= pkField.fieldNmPascal %>())
          <%_ }); -%>
            .build())
      <%_ } else { -%>
        .<%= field.fieldName %>(entity.get<%= field.fieldNmPascal %>())
      <%_ } -%>
    <%_ }); -%>
        .build();
  }

  @POST
  public <%= idJavaType %> save(@Valid <%= entityNmPascal %>Dto dto) {
    <%= entityNmPascal %>Entity entity = <%= entityNmPascal %>Entity.builder()
    <%_ fields.forEach((field) => { -%>
      <%_ if (compositePk && field.id) { -%>
        .<%= idField.fieldName %>(<%= idField.javaType %>.builder()
          <%_ compositePk.fields.forEach((pkField) => { -%>
            .<%= pkField.fieldName %>(dto.get<%= idField.fieldNmPascal %>().get<%= pkField.fieldNmPascal %>())
          <%_ }); -%>
            .build())
      <%_ } else { -%>
        .<%= field.fieldName %>(dto.get<%= field.fieldNmPascal %>())
      <%_ } -%>
    <%_ }); -%>
        .build();

    <%= entityNmPascal %>Entity savedEntity = <%= entityNmCamel %>Service.save(entity);

    <%_ if (compositePk) { -%>
    return <%= idJavaType %>.builder()
      <%_ compositePk.fields.forEach((pkField) => { -%>
        .<%= pkField.fieldName %>(savedEntity.get<%= idField.fieldNmPascal %>().get<%= pkField.fieldNmPascal %>())
      <%_ }); -%>
        .build();
    <%_ } else { -%>
    return savedEntity.get<%= idField.fieldNmPascal %>();
    <%_ } -%>
  }
}
