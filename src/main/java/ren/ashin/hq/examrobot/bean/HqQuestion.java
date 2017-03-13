package ren.ashin.hq.examrobot.bean;

import java.util.Date;

/**
 * @ClassName: HqQuestion
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public class HqQuestion {
    private Long id;
    private String name;
    private Long type;
    private Long sum;
    private Long radioSum;
    private Long checkboxSum;
    private Long truefalseSum;
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public Long getRadioSum() {
        return radioSum;
    }

    public void setRadioSum(Long radioSum) {
        this.radioSum = radioSum;
    }

    public Long getCheckboxSum() {
        return checkboxSum;
    }

    public void setCheckboxSum(Long checkboxSum) {
        this.checkboxSum = checkboxSum;
    }

    public Long getTruefalseSum() {
        return truefalseSum;
    }

    public void setTruefalseSum(Long truefalseSum) {
        this.truefalseSum = truefalseSum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
