<% include('../../lib/field-util', { fields }); -%>
package <%= domainPkgNm %>;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class <%= entityNmPascal %>Service {

  private final <%= entityNmPascal %>Repository <%= entityNmCamel %>Repository;

  public <%= entityNmPascal %>Entity find(<%= idJavaType %> id) {

    return <%= entityNmCamel %>Repository.findById(id).orElseThrow(IllegalArgumentException::new);
  }

  @Transactional
  public <%= entityNmPascal %>Entity save(<%= entityNmPascal %>Entity entity) {

    return <%= entityNmCamel %>Repository.save(entity);
  }
}
