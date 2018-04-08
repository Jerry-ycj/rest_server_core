package mizuki.project.core.restserver;

import mizuki.project.core.restserver.config.BasicMapDataRet;
import mizuki.project.core.restserver.config.BasicRet;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorController extends AbstractErrorController {

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping("/error")
    public BasicMapDataRet handle(HttpServletRequest request) {
        BasicMapDataRet ret  =new BasicMapDataRet();
        ret.setResult(BasicRet.ERR).setMessage((String) request.getAttribute("javax.servlet.error.message"));
        ret.getData().put("status", request.getAttribute("javax.servlet.error.status_code"));
        return ret;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
