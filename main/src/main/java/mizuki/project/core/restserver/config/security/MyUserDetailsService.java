package mizuki.project.core.restserver.config.security;

import mizuki.project.core.restserver.mod_user.UserMapper;
import mizuki.project.core.restserver.mod_user.bean.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ycj on 2016/11/28.
 *
 */
public class MyUserDetailsService  implements UserDetailsService {

    private UserMapper userMapper;

    public MyUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取真实的用户信息和权限
        System.out.println("调用了 userDetailServ: "+username);
        User user = null;
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(String.valueOf(user.getRole().getId())));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPwd(),true,true,true,true,authorities);
    }
}
