package mizuki.project.core.restserver.modules.oss_ali;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 只是用于 user 的common oss
 * 如果其他类型的 如customer，需要自行添加action
 */
//@RestController
@RequestMapping("/rest/common/oss")
@SessionAttributes({"user"})
public class CommonOssRestAction {

}
