package <%= domainPkgNm %>;

import org.springframework.data.jpa.repository.JpaRepository;

interface <%= entityNmPascal %>Repository extends JpaRepository<<%= entityNmPascal %>Entity, <%= idField.javaType %>> {
}
