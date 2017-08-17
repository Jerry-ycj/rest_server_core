package mizuki.project.core.restserver.config.security;

import mizuki.project.core.restserver.util.CodeUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ycj on 2016/11/29.
 *
 */
public class UsernamePwdAuthenFilter extends UsernamePasswordAuthenticationFilter {

//    private TestMapper mapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("login ----");
//        System.out.println(mapper.findOne().getPhone());

        String username = request.getParameter("username");
        String password = request.getParameter("pwd");
        password = CodeUtil.md5(password);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        this.setDetails(request, authRequest);
        return getAuthenticationManager().authenticate(authRequest);
    }

//    public UsernamePwdAuthenFilter setMapper(TestMapper mapper) {
//        this.mapper = mapper;
//        return this;
//    }
}
