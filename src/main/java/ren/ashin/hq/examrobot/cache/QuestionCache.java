package ren.ashin.hq.examrobot.cache;

import java.util.List;

import ren.ashin.hq.examrobot.ExamRobot;
import ren.ashin.hq.examrobot.bean.HqQuestion;
import ren.ashin.hq.examrobot.dao.HqQuestionDao;

import com.google.common.collect.Lists;

/**
 * @ClassName: QuestionCache
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public class QuestionCache {
    private List<HqQuestion> questionList = Lists.newArrayList();
    private HqQuestionDao hqQuestionDao = ExamRobot.ctx.getBean(HqQuestionDao.class);
    private static QuestionCache questionCache = new QuestionCache();

    private QuestionCache() {
    }
    public static QuestionCache getInstance(){
        return questionCache;
    }
    public List<HqQuestion> getQuestionList() {
        return questionList;
    }
    public void reCache() {
        questionList = hqQuestionDao.selectAllQuestion();
    }
}
