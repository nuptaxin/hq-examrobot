package ren.ashin.hq.examrobot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import ren.ashin.hq.examrobot.bean.HqQuestion;

/**
 * @ClassName: HqTaskDao
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public interface HqQuestionDao {
    @Select("select * from hq_question")
    List<HqQuestion> selectAllQuestion();
}
