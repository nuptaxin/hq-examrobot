package ren.ashin.hq.examrobot.cache;

import java.util.List;

import ren.ashin.hq.examrobot.ExamRobot;
import ren.ashin.hq.examrobot.bean.HqAnswer;
import ren.ashin.hq.examrobot.dao.HqAnswerDao;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * @ClassName: AnswerCache
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public class AnswerCache {
    private Multimap<Long, HqAnswer> answerMap = ArrayListMultimap.create();
    private HqAnswerDao hqAnswerDao = ExamRobot.ctx.getBean(HqAnswerDao.class);
    private static AnswerCache answerCache = new AnswerCache();

    private AnswerCache() {}

    public static AnswerCache getInstance() {
        return answerCache;
    }

    public List<HqAnswer> getAnswerByQId(Long id) {
        if (answerMap.containsKey(id)) {
            return (List<HqAnswer>) answerMap.get(id);
        }
        List<HqAnswer> hqAnswerList = hqAnswerDao.selectAnswerByQId(id);
        for (HqAnswer hqAnswer : hqAnswerList) {
            answerMap.put(hqAnswer.getCourseId(), hqAnswer);
        }
        return hqAnswerList;
    }

    public void clearAll() {
        answerMap.clear();
    }
}
