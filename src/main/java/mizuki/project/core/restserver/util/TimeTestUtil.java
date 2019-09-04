package mizuki.project.core.restserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 计算运行时间
 */
public class TimeTestUtil {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private long point = 0;
    private long sum = 0;
    private StringBuilder res;

    public void init(String info){
        point = System.currentTimeMillis();
        res = new StringBuilder();
        res.append("time_test ").append(info).append(": ");
        sum = 0;
    }

    public void point(String msg){
        long now = System.currentTimeMillis();
        res.append(msg).append(":").append(now-point).append("; ");
        sum+=(now-point);
        point = now;
    }

    public void print(){
        res.append(" . ").append(sum);
        logger.info(res.toString());
    }

    public static void main(String[] args) {
        TimeTestUtil testUtil = new TimeTestUtil();
        testUtil.init("testabc");
        testUtil.point("aa");
        testUtil.point("bb");
        testUtil.print();
    }
}
