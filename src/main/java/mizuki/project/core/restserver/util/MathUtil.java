package mizuki.project.core.restserver.util;

/**
 * Created by ycj on 2017/2/22.
 *
 */
public class MathUtil {

    /** double 四舍五入 保留2位  */
    public static double roundDouble(Double d){
        int t = (int)(d*1000);
        double tt = t*1.0/10;
        return Math.round(tt)*1.00/100;
    }
}
