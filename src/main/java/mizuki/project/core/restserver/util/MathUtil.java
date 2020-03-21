package mizuki.project.core.restserver.util;

import java.math.BigDecimal;
import java.util.List;

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

    /**
     * 求给定双精度数组中值的和
     */
    public static double getSum(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        double sum = 0;
        for (double inputDatum : inputData) {
            sum = sum + inputDatum;
        }
        return sum;
    }

    /**
     * 求给定双精度数组中值的平均值
     */
    public static double getAverage(double[] inputData) {
        if (inputData == null || inputData.length == 0)
            return -1;
        int len = inputData.length;
        double result;
        result = getSum(inputData) / len;
        return result;
    }

    /**
     * 求给定双精度数组中值的平方和
     */
    public static double getSquareSum(double[] inputData) {
        if(inputData==null||inputData.length==0)
            return -1;
        double sqrsum = 0.0;
        for (double inputDatum : inputData) {
            sqrsum = sqrsum + inputDatum * inputDatum;
        }
        return sqrsum;
    }

    /**
     * s^2=[(x1-x)^2 +...(xn-x)^2]/n
     */
    public static long getVariance(List<Long> list) {
        int count=list.size();
        long sum=list.stream().reduce(0L,Long::sum);
        long dAve=sum/count;//求平均值
        long dVar=0;
        for(Long l:list){
            dVar+=(l-dAve)*(l-dAve);
        }
        return dVar/count;
    }

    public static BigDecimal formatBigDecimal(Object val){
        if(val instanceof Double){
            return BigDecimal.valueOf((Double) val);
        }else if(val instanceof BigDecimal){
            return (BigDecimal)val;
        }else if(val instanceof Integer){
            return BigDecimal.valueOf((Integer) val);
        }else if(val instanceof Long){
            return BigDecimal.valueOf((Long)val);
        }else if(val instanceof String){
            try {
                return new BigDecimal((String) val);
            }catch (Exception e){
                return null;
            }
        }
        return null;
    }

    public static Long formatLong(Object val){
        if(val instanceof Long){
            return (long)val;
        }else if(val instanceof Integer){
            return ((Integer)val).longValue();
        }else if(val instanceof String){
            return Long.valueOf((String) val);
        }
        return 0L;
    }

    public static Integer formatInteger(Object val, Integer defaul){
        if(val instanceof Integer){
            return (Integer) val;
        }else if(val instanceof Long){
            return ((Long)val).intValue();
        }else if(val instanceof String){
            return Integer.valueOf((String) val);
        }
        return defaul;
    }

    /**
     * 求给定双精度数组中值的标准差
     */
    public static double getStandardDeviation(List<Long> inputData) {
        return Math.sqrt(getVariance(inputData));
    }
}
