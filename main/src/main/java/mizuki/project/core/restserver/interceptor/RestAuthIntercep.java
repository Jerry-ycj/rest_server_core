package mizuki.project.core.restserver.interceptor;

import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.security.SecurityConfig;
import mizuki.project.core.restserver.mod_user.UserMapper;
import mizuki.project.core.restserver.mod_user.bean.User;
import mizuki.project.core.restserver.util.JsonUtil;
import mizuki.project.core.restserver.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by ycj on 2016/12/10.
 * rest auth token
 */
public class RestAuthIntercep extends HandlerInterceptorAdapter {

    @Autowired
    private UserMapper userMapper;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User u = (User)session.getAttribute("user");
        if(u==null){
            String token = request.getParameter("token");
            if(token!=null) {
                u = userMapper.findUserByToken(token,20);
                if (u != null) {
                    session.setAttribute("user", u);
                    SecurityConfig.authStore(u);
                    return true;
                } else {
                    ResponseUtil.setCross(response,request);
                    response.getWriter().print(JsonUtil.toJson(new BasicRet(BasicRet.TOKEN_ERR,"token error")));
                    return false;
                }
            }else{
                ResponseUtil.setCross(response,request);
                response.getWriter().print(JsonUtil.toJson(new BasicRet(BasicRet.TOKEN_ERR,"token error")));
                return false;
            }
        }else{
            SecurityConfig.authStore(u);
            return true;
        }
    }
}
