package mizuki.project.core.restserver;

import mizuki.project.core.restserver.config.WebConfBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by ycj on 2017/3/22.
 * 用于所有的Action
 */
@Deprecated
public abstract class AbstractAction {

    private WebConfBean webConfBean;

    @Autowired
    public AbstractAction setWebConfBean(WebConfBean webConfBean) {
        this.webConfBean = webConfBean;
        return this;
    }

    @ModelAttribute("confBean")
    public WebConfBean webConfBean(){
        return webConfBean;
    }

}
