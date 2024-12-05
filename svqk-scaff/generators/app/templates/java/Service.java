package <%= domainPkgName %>;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class <%= entNamePascal %>Service {

  private final <%= entNamePascal %>Repository <%= entNameCamel %>Repository;

  public <%= entNamePascal %>Entity find(int id) {

    return <%= entNameCamel %>Repository.findById(id).orElseThrow(IllegalArgumentException::new);
  }

  @Transactional
  public <%= entNamePascal %>Entity save(<%= entNamePascal %>Entity entity) {

    return <%= entNameCamel %>Repository.save(entity);
  }
}
