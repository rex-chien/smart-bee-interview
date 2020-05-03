package rex.chien.cms.security;

import org.springframework.stereotype.Component;

@Component
public interface RoleName {
    String PREFIX = "ROLE_";

    String SUPER_USER = "SUPER_USER";

    String MANAGER = "MANAGER";

    String OPERATOR = "OPERATOR";
}
