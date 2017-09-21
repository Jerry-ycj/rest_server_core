package mizuki.project.core.restserver.modules.oss_ali;

import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.mod_user.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * 只是用于 user 的common oss
 * 如果其他类型的 如customer，需要自行添加action
 */
@RestController
@RequestMapping("/rest/common/oss")
@SessionAttributes({"user"})
public class CommonOssRestAction {

}
