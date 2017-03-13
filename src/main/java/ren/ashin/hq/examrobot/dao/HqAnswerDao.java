package ren.ashin.hq.examrobot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import ren.ashin.hq.examrobot.bean.HqAnswer;

/**
 * @ClassName: HqTaskDao
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public interface HqAnswerDao {
    @Select("select * from hq_answer where questionid = #{questionId}")
    List<HqAnswer> selectAnswerByQId(Long questionId);
}