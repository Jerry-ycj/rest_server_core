package mizuki.project.core.restserver.interceptor;

import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.security.SecurityConfig;
import mizuki.project.core.restserver.mod_user.UserCenter;
import mizuki.project.core.restserver.mod_user.bean.User;
import mizuki.project.core.restserver.util.JsonUtil;
import mizuki.project.core.restserver.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by ycj on 2016/12/10.
 * rest auth token
 */
public class RestAuthIntercep extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserCenter userCenter;

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
                        if(!checkUser(u, response,request)) return false;
                        // 复制session内容
                        session1.getAttributeNames().forEach(name->{
                            session.setAttribute(name,session1.getAttribute(name));
                        });
                        SecurityConfig.authStore(u);
                        return true;
                    }
                }
            }
            resError(response,request,new BasicRet(BasicRet.TOKEN_ERR,"登录失效"));
            return false;
        }else{
            if(!checkUser(u, response,request)) return false;
            SecurityConfig.authStore(u);
            return true;
        }
    }

    // 用于用户信息的同步
    private boolean checkUser(User u, HttpServletResponse response, HttpServletRequest request) throws IOException {
        if(userCenter.get(u)==null) {
            userCenter.add(u);
            return true;
        }
        User newUser = userCenter.get(u);
        u.setOff(newUser.getOff()).setAddress(newUser.getAddress()).setExtend(newUser.getExtend()).setGender(newUser.getGender()).setImage(newUser.getImage()).setName(newUser.getName()).setUsername(newUser.getUsername()).setPhone(newUser.getPhone()).setPwd(newUser.getPwd()).setRole(newUser.getRole());
        if(u.getOff()==User.OFF_DEL){
            resError(response,request,new BasicRet(BasicRet.ERR, "用户不存在"));
            return false;
        }else if(u.getOff()==User.OFF_FREEZE){
            resError(response,request,new BasicRet(BasicRet.ERR, "用户已冻结"));
            return false;
        }
        return true;
    }

    private void resError(HttpServletResponse response, HttpServletRequest request, BasicRet basicRet) throws IOException {
        ResponseUtil.setCross(response,request);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getWriter().print(JsonUtil.toJson(basicRet));
    }
}
