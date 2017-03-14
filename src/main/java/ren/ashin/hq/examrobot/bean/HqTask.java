package ren.ashin.hq.examrobot.bean;

import java.util.Date;

/**
 * @ClassName: HqTask
 * @Description: 任务表
 * @author renzx
 * @date Mar 13, 2017
 */
public class HqTask {
    public static final Long NOT_RUN = 0L;
    public static final Long RUNNING = 1L;
    public static final Long SUCCESS = 2L;
    public static final Long FAILED = 3L;
    
    private Long id;
    private Long userId;
    private Long courseId;
    private Date createTime;
    private Date updateTime;
    private Long status;
    private String comment;

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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
