<% include('../../lib/field-util', { fields }); -%>
package <%= domainPkgNm %>;

import org.springframework.data.jpa.repository.JpaRepository;

interface <%= entityNmPascal %>Repository extends JpaRepository<<%= entityNmPascal %>Entity, <%= idJavaType %>> {
}
