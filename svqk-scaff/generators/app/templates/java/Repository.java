package <%= domainPkgName %>;

import org.springframework.data.jpa.repository.JpaRepository;

interface <%= entNamePascal %>Repository extends JpaRepository<<%= entNamePascal %>Entity, Integer> {
}
