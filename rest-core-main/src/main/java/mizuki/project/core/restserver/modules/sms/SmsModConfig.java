package mizuki.project.core.restserver.modules.sms;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DayuSms.class,
        CommonSmsRestAction.class
})
public class SmsModConfig {
}
