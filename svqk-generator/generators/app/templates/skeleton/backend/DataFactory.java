<% include('../../lib/data-factory-def'); -%>
package <%= interfacesPkgNm %>;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
<%= dfdef.imports %>

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class <%= entityNmPascal %>DataFactory {

  public static <%= entityNmPascal %>Dto create<%= entityNmPascal %>() {
    return <%= entityNmPascal %>Dto.builder()
<%= dfdef.idBuilder %>
<%= dfdef.nonIdsBuilder %>
        .build();
  }
}