package mizuki.project.core.restserver.config;

import mizuki.project.core.restserver.config.exception.RestMainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ycj on 15/11/24.
 *
 */
@ControllerAdvice
public class ExceptionProcess {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(RestMainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody BasicRet handle(RestMainException e){
        logger.error("业务错误：",e);
        return new BasicRet(BasicRet.ERR,e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody BasicRet handle(AccessDeniedException e){
        return new BasicRet(BasicRet.ERR,"用户权限不足");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody BasicRet handle(Exception e){
        logger.error("err：",e);
        return new BasicRet(BasicRet.ERR,"服务器错误"+(e.getMessage()==null?"":": "+e.getMessage()));
    }
}
