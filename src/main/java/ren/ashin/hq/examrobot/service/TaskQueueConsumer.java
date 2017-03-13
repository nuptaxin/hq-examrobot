package ren.ashin.hq.examrobot.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ren.ashin.hq.examrobot.ExamRobot;
import ren.ashin.hq.examrobot.bean.HqAnswer;
import ren.ashin.hq.examrobot.bean.HqQuestion;
import ren.ashin.hq.examrobot.bean.HqTask;
import ren.ashin.hq.examrobot.bean.HqUser;
import ren.ashin.hq.examrobot.cache.AnswerCache;
import ren.ashin.hq.examrobot.cache.QuestionCache;
import ren.ashin.hq.examrobot.dao.HqTaskDao;
import ren.ashin.hq.examrobot.dao.HqUserDao;

/**
 * @ClassName: TaskQueueConsumer
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public class TaskQueueConsumer extends Thread {
    private TaskQueueService taskQueueService = ExamRobot.ctx.getBean(TaskQueueService.class);
    private HqTaskDao hqTaskDao = ExamRobot.ctx.getBean(HqTaskDao.class);
    private HqUserDao hqUserDao = ExamRobot.ctx.getBean(HqUserDao.class);
    private static final Logger LOG = Logger.getLogger(TaskQueueConsumer.class);

    @Override
    public void run() {
        while (true) {
            HqTask task = null;
            try {
                task = taskQueueService.getQueue().take();
                comsumerTask(task);
                taskQueueService.setTaskSize(taskQueueService.getQueue().size());
            } catch (InterruptedException e) {

            }
        }
    }

    private void comsumerTask(HqTask task) {
        task.setStatus(HqTask.RUNNING);
        task.setUpdateTime(new Date());
        hqTaskDao.updateTaskStatus(task);
        LOG.debug("已经更改任务状态为执行中，任务id：" + task.getId());
        
        // 获取用户信息
        HqUser user = hqUserDao.findUserById(task.getUserId());
        // 查看用户的积分信息，分析用户是否有积分答题。（暂时不做）
        
        List<HqQuestion> questionList = QuestionCache.getInstance().getQuestionList();
        //查看当前题库中该题的答案存储情况。如果没有存储过该题目，应该如何应对
        
        // 缓存当前的所有关于选择问题的答案
        List<HqAnswer> answerList = AnswerCache.getInstance().getAnswerByQId(task.getQuestionId());
        
        
    }

}
