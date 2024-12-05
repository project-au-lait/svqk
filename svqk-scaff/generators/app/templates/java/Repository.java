package <%= interfacesPkgName %>;

import org.springframework.data.jpa.repository.JpaRepository;

public class <%= entNamePascal %>Repository extends JpaRepository<<%= entNamePascal %>Entity, Integer> { }
