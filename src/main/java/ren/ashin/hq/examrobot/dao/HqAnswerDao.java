package ren.ashin.hq.examrobot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import ren.ashin.hq.examrobot.bean.HqAnswer;

/**
 * @ClassName: HqTaskDao
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public interface HqAnswerDao {
    @Select("select * from hq_answer where courseid = #{questionId}")
    List<HqAnswer> selectAnswerByQId(Long courseiId);

    @Insert("insert into hq_answer(courseid,subjectid,name,type,answera,answerb,answerc,answerd,answere,answerf,answerletter,createtime,time)"
            + "value(#{courseId},#{subjectId},#{name},#{type},#{answerA},#{answerB},#{answerC},#{answerD},#{answerE},#{answerF},#{answerLetter},#{createTime},0)")
    void insertAnswer(HqAnswer hqAnswer);
}
