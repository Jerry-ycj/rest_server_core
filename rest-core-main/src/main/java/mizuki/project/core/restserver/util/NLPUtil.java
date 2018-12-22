package mizuki.project.core.restserver.util;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NLPUtil {

    private static List<String> ignoreNaturesHanLP;
    private static String[] ignoreNaturesHanLPArr = new String[]{
        "ul","uj","w","d","dg","dl","c","cc","p","pba","pbei"
    };

    @PostConstruct
    public void init(){
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

    /**
     * for 保存索引
     */
    public String seqForPgVector(String... origin){
        // todo 处理同义词
        return tranlatePGVector(seq_hanlp(origin));
    }

    /**
     * seq for pgsql tsquery , or关系. for 查询字段
     */
    public String seqForPgQuery(String... origin){
        return tranlatePGQuery(seq_hanlp(origin));
    }

    private List seq_hanlp(String... origin){
        String strs = combine(origin);
        List<Term> list = IndexTokenizer.segment(strs);
        List<String> ret = new ArrayList<>();
        list.forEach(term -> {
            if(!ignoreNaturesHanLP.contains(term.nature.name())) ret.add(term.word);
        });
        return ret;
    }

    private String combine(String... strs){
        if(strs.length==0) return "";
        if(strs.length==1) return strs[0];
        StringBuilder stringBuilder = new StringBuilder();
        for(String s:strs){
            stringBuilder.append(s).append(" ");
        }
        return stringBuilder.deleteCharAt(stringBuilder.length()-1).toString();
    }

    /**
     * translate for tsvector
     */
    private String tranlatePGVector(Collection list){
        if(list.size()==0) return "";
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(c->stringBuilder.append(c).append(" "));
        return stringBuilder.deleteCharAt(stringBuilder.length()-1).toString();
    }
    /**
     * translate for tsquery
     */
    private String tranlatePGQuery(Collection list){
        if(list.size()==0) return "";
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(c->stringBuilder.append(c).append("|"));
        return stringBuilder.deleteCharAt(stringBuilder.length()-1).toString();
    }
}
