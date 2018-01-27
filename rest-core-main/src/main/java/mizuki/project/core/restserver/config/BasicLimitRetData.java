package mizuki.project.core.restserver.config;

import io.swagger.annotations.ApiModelProperty;

public class BasicLimitRetData {
    @ApiModelProperty(notes = "总页数")
    protected Integer totalPages;
    @ApiModelProperty(notes = "总数")
    protected Integer totalNums;
    @ApiModelProperty(notes = "第几页，0开始")
    protected Integer offset;
    @ApiModelProperty(notes = "一页实际个数")
    protected Integer num;

    public Integer getTotalPages() {
        return totalPages;
    }

    public BasicLimitRetData setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public Integer getTotalNums() {
        return totalNums;
    }

    public BasicLimitRetData setTotalNums(Integer totalNums) {
        this.totalNums = totalNums;
        return this;
    }

    public Integer getOffset() {
        return offset;
    }

    public BasicLimitRetData setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Integer getNum() {
        return num;
    }

    public BasicLimitRetData setNum(Integer num) {
        this.num = num;
        return this;
    }
}
