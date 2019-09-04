package mizuki.project.core.restserver.interceptor;

import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.security.SecurityConfig;
import mizuki.project.core.restserver.mod_user.bean.User;
import mizuki.project.core.restserver.util.JsonUtil;
import mizuki.project.core.restserver.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by ycj on 2016/12/10.
 * rest auth token
 */
public class RestAuthIntercep extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User u = (User)session.getAttribute("user");
        // 用于 session操作时 可以获取session
        if(session.getAttribute("sessionId")==null) session.setAttribute("sessionId",session.getId());
        if(u==null){
            // token 用于在session有效期内的 重新获得session
            String token = request.getParameter("token");
            if(token!=null) {
                Session session1 = sessionRepository.findById(token);
                if(session1!=null){
                    u = session1.getAttribute("user");
                    if (u != null) {
                        // 复制session内容
                        session1.getAttributeNames().forEach(name->{
                            session.setAttribute(name,session1.getAttribute(name));
                        });
                        SecurityConfig.authStore(u);
                        return true;
                    }
                }
            }
            ResponseUtil.setCross(response,request);
            response.getWriter().print(JsonUtil.toJson(new BasicRet(BasicRet.TOKEN_ERR,"登录失效")));
            return false;
        }else{
            SecurityConfig.authStore(u);
            return true;
        }
    }
}
