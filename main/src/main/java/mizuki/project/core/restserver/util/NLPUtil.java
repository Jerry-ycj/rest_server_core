package mizuki.project.core.restserver.util;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;

import java.util.*;
import java.util.stream.Collectors;

public class NLPUtil {

    private static List<String> ignoreNaturesHanLP;
    private static String[] ignoreNaturesHanLPArr = new String[]{
        "ul","uj","w","d","dg","dl","c","cc","p","pba","pbei"
    };

    static {
//        IndexAnalysis.parse("初始化 init");
//        new KeyWordComputer(1).computeArticleTfidf("初始化 init");
        IndexTokenizer.segment("初始化 init");
        HanLP.extractKeyword("初始化 init",1);
        ignoreNaturesHanLP= Arrays.stream(ignoreNaturesHanLPArr).collect(Collectors.toList());
    }

//    public static void seq_ansj(String... origin){
//        String strs = combine(origin);
//        Result result = IndexAnalysis.parse(strs);
//        Set<String> set = new HashSet<>();
//        result.getTerms().forEach(term -> {
//            if(!ignoreNaturesHanLP.contains(term.getNatureStr())) set.add(term.getName());
//        });
//        System.out.println(result);
//        System.out.println(tranlate2pg(set));
//    }

    public static String seqForPgVector(String... origin){
        return tranlatePGVector(seq_hanlp(origin));
    }

    /**
     * seq for pgsql tsquery , or关系
     */
    public static String seqForPgQuery(String... origin){
        return tranlatePGQuery(seq_hanlp(origin));
    }

    private static List seq_hanlp(String... origin){
        String strs = combine(origin);
        List<Term> list = IndexTokenizer.segment(strs);
        List<String> ret = new ArrayList<>();
        list.forEach(term -> {
            if(!ignoreNaturesHanLP.contains(term.nature.name())) ret.add(term.word);
        });
        return ret;
    }

    private static String combine(String... strs){
        if(strs.length==0) return "";
        if(strs.length==1) return strs[0];
        StringBuilder stringBuilder = new StringBuilder();
        for(String s:strs){
            stringBuilder.append(s).append(" ");
        }
        return stringBuilder.deleteCharAt(stringBuilder.length()-1).toString();
    }

    /**
     * translate for tsvector or tsquery
     */
    private static String tranlatePGVector(Collection list){
        if(list.size()==0) return "";
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(c->stringBuilder.append(c).append(" "));
        return stringBuilder.deleteCharAt(stringBuilder.length()-1).toString();
    }
    /**
     * translate for tsquery
     */
    private static String tranlatePGQuery(Collection list){
        if(list.size()==0) return "";
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(c->stringBuilder.append(c).append("|"));
        return stringBuilder.deleteCharAt(stringBuilder.length()-1).toString();
    }

//    public static void main(String[] args) {
//        String[] str = new String[]{
//                "太平鸟男装冬季新款黑色轻薄韩版短款保暖立领羽绒服外套男立领有型 超轻面料 嵌入式罗纹袖口",
//                "PEACEBIRD/太平鸟",
//        };
//        String search_str = "不锈钢螺钉 王国强 嵌入式 控制板";
//
//        long a = System.currentTimeMillis();
//        System.out.println(seqForPg(str));
//        System.out.println(seqForPg(search_str));
//        long b = System.currentTimeMillis();
//        System.out.println("use: "+(b-a));
//
////        System.out.println("");
////
////        a = System.currentTimeMillis();
////        seq_ansj(str);
////        seq_ansj(search_str);
////        b = System.currentTimeMillis();
////        System.out.println("use: "+(b-a));
//    }
}
