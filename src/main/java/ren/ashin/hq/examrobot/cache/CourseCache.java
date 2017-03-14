package ren.ashin.hq.examrobot.cache;

import java.util.List;

import ren.ashin.hq.examrobot.ExamRobot;
import ren.ashin.hq.examrobot.bean.HqCourse;
import ren.ashin.hq.examrobot.dao.HqCourseDao;

import com.google.common.collect.Lists;

/**
 * @ClassName: QuestionCache
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public class CourseCache {
    private List<HqCourse> questionList = Lists.newArrayList();
    private HqCourseDao hqQuestionDao = ExamRobot.ctx.getBean(HqCourseDao.class);
    private static CourseCache questionCache = new CourseCache();

    private CourseCache() {
    }
    public static CourseCache getInstance(){
        return questionCache;
    }
    public List<HqCourse> getQuestionList() {
        return questionList;
    }
    public void reCache() {
        questionList = hqQuestionDao.selectAllCourse();
    }
}
