package ren.ashin.hq.examrobot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import ren.ashin.hq.examrobot.bean.HqCourse;

/**
 * @ClassName: HqTaskDao
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public interface HqCourseDao {
    @Select("select * from hq_course")
    List<HqCourse> selectAllCourse();
}
