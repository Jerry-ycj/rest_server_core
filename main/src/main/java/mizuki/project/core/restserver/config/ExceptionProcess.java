package mizuki.project.core.restserver.config;

import mizuki.project.core.restserver.config.exception.RestMainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public @ResponseBody
    Map<String,Object> handle(RestMainException e){
        Map<String,Object> data = new HashMap<>();
        data.put("result",0);
        data.put("message","服务器错误"+(e.getMessage()==null?"":": "+e.getMessage()));
        logger.error("err：",e);
        // todo deal model
        return data;
    }
}