package ren.ashin.hq.examrobot.job;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ren.ashin.hq.examrobot.ExamRobot;
import ren.ashin.hq.examrobot.bean.HqTask;
import ren.ashin.hq.examrobot.dao.HqTaskDao;
import ren.ashin.hq.examrobot.service.TaskQueueService;

import com.google.common.collect.Lists;

/**
 * @ClassName: ScanJob
 * @Description: 任务扫描job
 * @author renzx
 * @date Nov 11, 2016
 */
public class ScanTaskJob implements Job {
    private static final Logger LOG = Logger.getLogger(ScanTaskJob.class);
    private TaskQueueService taskQueueService = ExamRobot.ctx.getBean(TaskQueueService.class);
    private HqTaskDao hqTaskDao = ExamRobot.ctx.getBean(HqTaskDao.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date fireTime = context.getFireTime();
        exeTask(fireTime);
    }

    private void exeTask(Date fireTime) {
        if (taskQueueService.getTaskSize() == 0) {
            LOG.info("任务扫描执行中" + new DateTime(fireTime).toString("yyyy-MM-dd HH:mm:ss"));
            List<HqTask> taskList = Lists.newArrayList();
            taskList = hqTaskDao.findNotRunTask();
            LOG.debug("检测到未执行任务数量：" + taskList.size());
            for (HqTask task : taskList) {
                try {
                    taskQueueService.getQueue().put(task);
                    taskQueueService.setTaskSize(taskQueueService.getTaskSize()+1);
                } catch (InterruptedException e) {
                    LOG.warn("扫描任务加入队列出错", e);
                }
            }
        }
    }
    

}
