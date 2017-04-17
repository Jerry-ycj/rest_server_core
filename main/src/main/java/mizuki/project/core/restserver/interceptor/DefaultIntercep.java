package mizuki.project.core.restserver.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import mizuki.project.core.restserver.config.WebConfBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalTime;
import java.util.Enumeration;

/**
 * Created by ycj on 16/4/15.
 *
 */
public class DefaultIntercep extends HandlerInterceptorAdapter {

    private WebConfBean wcb;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public DefaultIntercep setWcb(WebConfBean wcb) {
        this.wcb = wcb;
        return this;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("|--"+wcb.getContextPath()+" ----------- request: \n");
        sb.append(LocalTime.now())
                .append(", ").append(request.getRequestURI())
                .append(" ").append(request.getHeader("X-Real-IP"))
                .append(" X-Forwarded-Proto:")
                .append(request.getHeader("X-Forwarded-Proto")).append("\n");
        sb.append("sessionid: ")
                .append(request.getSession().getId()).append(" \n");
        Enumeration<String> e = request.getParameterNames();
        while (e.hasMoreElements()){
            String param = e.nextElement();
            sb.append(param).append(":")
                    .append(request.getParameter(param)).append("\n");
        }
        sb.append("^^^^|");
        logger.info(sb.toString());
        return true;
    }
}
