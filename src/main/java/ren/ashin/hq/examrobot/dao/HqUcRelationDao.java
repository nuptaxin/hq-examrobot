package ren.ashin.hq.examrobot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import ren.ashin.hq.examrobot.bean.HqUcRelation;

/**
 * @ClassName: HqTaskDao
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public interface HqUcRelationDao {
    @Select("select * from hq_ucrelation")
    List<HqUcRelation> selectAllRelation();
    
    @Select("select * from hq_ucrelation")
    List<HqUcRelation> selectRelationByUserId();
    
    @Delete("delete from hq_ucrelation where userid = #{userId}")
    void deleteRelationByUserId(Long userId);

    @Insert("insert hq_ucrelation(userid,courseid,status,createtime,updatetime) values(#{userId},#{courseId},#{status},#{createTime},#{updateTime})")
    void listInsert(HqUcRelation hqUcRelation);
}
