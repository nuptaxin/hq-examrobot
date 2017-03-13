package ren.ashin.hq.examrobot.bean;

import java.util.Date;

/**
 * @ClassName: HqPointRecord
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public class HqPointRecord {
    private Long id;
    private Long userId;
    private String comment;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
