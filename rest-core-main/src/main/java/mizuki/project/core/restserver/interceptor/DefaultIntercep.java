package mizuki.project.core.restserver.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Created by ycj on 16/4/15.
 *
 */
public class DefaultIntercep extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession();
        if(session.getAttribute("sessionId")==null) session.setAttribute("sessionId",session.getId());
        StringBuilder sb = new StringBuilder();
        sb.append(" -------- request: \n");
        sb.append(request.getRequestURI())
            .append(" ").append(request.getHeader("X-Real-IP"))
            .append(" X-Forwarded-Proto:")
            .append(request.getHeader("X-Forwarded-Proto")).append("\n");
        sb.append("sessionid: ").append(session.getId()).append(" \n");
        Enumeration<String> e = request.getParameterNames();
        while (e.hasMoreElements()){
            String param = e.nextElement();
            sb.append(param).append(":")
                    .append(request.getParameter(param)).append("\n");
        }
        logger.info(sb.toString());
        return true;
    }
}
