<% include('../../lib/interface-common'); -%>
<%_
getPath = ifcom.idFields.map((field) => `{${field.fieldName}}`).join("/");
getMethodArgs = ifcom.buildArgs((field) => `@PathParam("${field.fieldName}") ${field.javaType} ${field.fieldName}`);
-%>
package <%= interfacesPkgNm %>;

import <%= domainPkgNm %>.<%= entityNmPascal %>Entity;
<%_ if(compIdFields) { -%>
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
  <%_ if (compIdFields) { -%>
    <%= entityNmPascal %>Entity entity =
        <%= entityNmCamel %>Service.find(
            <%= idField.javaType %>.builder()
              <%_ compIdFields.forEach((compIdField) => { -%>
                .<%= compIdField.fieldName %>(<%= compIdField.fieldName %>)
              <%_ }); -%>
                .build());
  <%_ } else { -%>
    <%= entityNmPascal %>Entity entity = <%= entityNmCamel %>Service.find(<%= idField.fieldName %>);
  <%_ } -%>

    return <%= entityNmPascal %>Dto.builder()
    <%_ fields.forEach((field) => { -%>
      <%_ if (compIdFields && field.id) { -%>
        .<%= field.fieldName %>(<%= ifcom.interfaceIdType %>.builder()
          <%_ compIdFields.forEach((compIdField) => { -%>
            .<%= compIdField.fieldName %>(entity.get<%= idField.fieldNmPascal %>().get<%= compIdField.fieldNmPascal %>())
          <%_ }); -%>
            .build())
      <%_ } else { -%>
        .<%= field.fieldName %>(entity.get<%= field.fieldNmPascal %>())
      <%_ } -%>
    <%_ }); -%>
        .build();
  }

  @POST
  public <%= ifcom.interfaceIdType %> save(@Valid <%= entityNmPascal %>Dto dto) {
    <%= entityNmPascal %>Entity entity = <%= entityNmPascal %>Entity.builder()
    <%_ fields.forEach((field) => { -%>
      <%_ if (compIdFields && field.id) { -%>
        .<%= idField.fieldName %>(<%= idField.javaType %>.builder()
          <%_ compIdFields.forEach((compIdField) => { -%>
            .<%= compIdField.fieldName %>(dto.get<%= idField.fieldNmPascal %>().get<%= compIdField.fieldNmPascal %>())
          <%_ }); -%>
            .build())
      <%_ } else { -%>
        .<%= field.fieldName %>(dto.get<%= field.fieldNmPascal %>())
      <%_ } -%>
    <%_ }); -%>
        .build();

    <%= entityNmPascal %>Entity savedEntity = <%= entityNmCamel %>Service.save(entity);

    <%_ if (compIdFields) { -%>
    return <%= ifcom.interfaceIdType %>.builder()
      <%_ compIdFields.forEach((compIdField) => { -%>
        .<%= compIdField.fieldName %>(savedEntity.get<%= idField.fieldNmPascal %>().get<%= compIdField.fieldNmPascal %>())
      <%_ }); -%>
        .build();
    <%_ } else { -%>
    return savedEntity.get<%= idField.fieldNmPascal %>();
    <%_ } -%>
  }
}
