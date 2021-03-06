package ren.ashin.hq.examrobot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
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
    
    @Select("select * from hq_course where id=#{id}")
    HqCourse selectCourseById(Long id);
    
    @Select("select id from hq_course where name=#{name}")
    Long selectCourseIdByName(String name);

    @Insert("insert hq_course(name,type,createtime,updatetime,uuid) values(#{name},#{type},#{createTime},#{updateTime},#{uuId})")
    void listInsert(HqCourse hqCourse);
}
