package ren.ashin.hq.examrobot.bean;

import java.util.Date;


/**
 * @ClassName: HqParse
 * @Description: 解析记录表
 * @author renzx
 * @date Mar 13, 2017
 */
public class HqParse {

    private Long id;
    private Long courseId;
    private Date parseTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Date getParseTime() {
        return parseTime;
    }

    public void setParseTime(Date parseTime) {
        this.parseTime = parseTime;
    }
}
