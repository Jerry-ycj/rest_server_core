package mizuki.project.core.restserver.modules.oss_ali;

import com.aliyuncs.exceptions.ClientException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import mizuki.project.core.restserver.config.BasicMapDataRet;
import mizuki.project.core.restserver.config.BasicRet;
import mizuki.project.core.restserver.config.exception.RestMainException;
import mizuki.project.core.restserver.mod_user.bean.User;
import mizuki.project.core.restserver.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rest/oss")
@SessionAttributes({"user"})
@Api(tags = "ali oss模块")
public class CommonOssRestAction {

    @Autowired
    private AliOSS aliOSS;

    @RequestMapping(value="/sts",method={RequestMethod.POST})
    @ApiOperation(value = "获取sts凭证")
    public BasicMapDataRet sts(
            Model model,
            @ApiParam(required = true,value = "path list json: 如 folder/test.png")
            @RequestParam String pathJson
    ) throws RestMainException, ClientException {
        BasicMapDataRet ret = new BasicMapDataRet();
        User user = (User) model.asMap().get("user");
        List<String> list = JsonUtil.toObject(pathJson,List.class);
        if(list== null || list.size()==0) throw new RestMainException("path错误");
        ret.setData(aliOSS.stsGetPutForUser("user-"+user.getId(),list));
        ret.setResult(BasicRet.SUCCESS);
        return ret;
    }
}
