package mizuki.project.core.restserver.util;

import mizuki.project.core.restserver.config.BasicLimitRetData;

/**
 * 分页相关
 */
public class PageHelper {

    private Integer offset;
    private Integer num;
    private Integer sum;
    private int listSize;
    private BasicLimitRetData retData;

    public PageHelper(Integer offset, Integer num, BasicLimitRetData retData) {
        this.offset = offset;
        this.num = num;
        this.retData = retData;
    }

    /**
     * 是否需要分页
     */
    public boolean needPage(){
        return offset!=null && num!=null && offset>=0 && num>0;
    }

    public String getPgParams(){
        String params = "";
        if(needPage()){
            params = "limit "+num+" offset "+offset;
        }
        return params;
    }

    public void fill(){
        if(needPage()){
            retData.setTotalPages((sum+num-1)/num);
            retData.setOffset(offset);
        }else{
            retData.setTotalPages(1);
            retData.setOffset(0);
        }
        retData.setNum(listSize).setTotalNums(sum);
    }

    public Integer getOffset() {
        return offset;
    }

    public PageHelper setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Integer getNum() {
        return num;
    }

    public PageHelper setNum(Integer num) {
        this.num = num;
        return this;
    }

    public Integer getSum() {
        return sum;
    }

    public PageHelper setSum(Integer sum) {
        this.sum = sum;
        return this;
    }

    public int getListSize() {
        return listSize;
    }

    public PageHelper setListSize(int listSize) {
        this.listSize = listSize;
        return this;
    }

    public BasicLimitRetData getRetData() {
        return retData;
    }

    public PageHelper setRetData(BasicLimitRetData retData) {
        this.retData = retData;
        return this;
    }
}
