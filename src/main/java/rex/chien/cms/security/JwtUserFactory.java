package rex.chien.cms.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import rex.chien.cms.domain.Role;
import rex.chien.cms.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public class JwtUserFactory {
    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getAccount(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRoles())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> authorities) {
        return authorities.stream()
                .map(role -> RoleName.PREFIX + role.getName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
