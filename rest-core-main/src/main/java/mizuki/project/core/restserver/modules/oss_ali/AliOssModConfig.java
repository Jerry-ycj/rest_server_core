package mizuki.project.core.restserver.modules.oss_ali;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AliOSS.class,
        CommonOssRestAction.class
})
public class AliOssModConfig {

//    @Bean
//    public AliOSS aliOSS(){
//        return new AliOSS();
//    }
//
//    @Bean
//    public CommonOssRestAction commonOssRestAction(){
//        return new CommonOssRestAction();
//    }

}
