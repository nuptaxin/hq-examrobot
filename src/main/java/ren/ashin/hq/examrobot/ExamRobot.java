package ren.ashin.hq.examrobot;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.aeonbits.owner.ConfigFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ren.ashin.hq.examrobot.cache.CourseCache;
import ren.ashin.hq.examrobot.job.ScanTaskJob;
import ren.ashin.hq.examrobot.job.ScanUserJob;
import ren.ashin.hq.examrobot.service.TaskQueueConsumer;
import ren.ashin.hq.examrobot.util.MainConfig;

/**
 * @ClassName: ExamRobot
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public class ExamRobot {
    /**
     * @Fields LOG : TODO
     */
    private static final Logger LOG = Logger.getLogger(ExamRobot.class);
    public static Scheduler scheduler = null;
    public static ApplicationContext ctx = null;

    public static JobKey jobKey = null;

    public static MainConfig mfg = null;

    public static void main(String[] args) {
        PropertyConfigurator.configure("conf/log4j-hq.properties");
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        mfg = ConfigFactory.create(MainConfig.class);
        
        initCache();
        
        try {
            initScheduler();
        } catch (SchedulerException e) {
            LOG.error("初始化定时器出错", e);
        }
    }

    private static void initCache() {
        CourseCache.getInstance().reCache();;
        
    }

    public static void initScheduler() throws SchedulerException {
        // 调度任务工厂
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();

        // 任务表扫描任务
        JobDetail scanJobDetail = newJob(ScanTaskJob.class).withIdentity("任务表扫描", "group1").build();
        jobKey = scanJobDetail.getKey();
        CronTrigger scanTrigger =
                newTrigger().withIdentity("trigger1", "group1")
                        .withSchedule(cronSchedule(mfg.cronTask())).build();
        scheduler.scheduleJob(scanJobDetail, scanTrigger);
        
        // 用户表扫描任务
        JobDetail scanUserDetail = newJob(ScanUserJob.class).withIdentity("用户表扫描", "group1").build();
        CronTrigger scanUserTrigger =
                newTrigger().withIdentity("trigger2", "group1")
                .withSchedule(cronSchedule(mfg.cronUser())).build();
        scheduler.scheduleJob(scanUserDetail, scanUserTrigger);

        scheduler.start();

        // 启动任务消费进程（单线程）
        TaskQueueConsumer mcs = new TaskQueueConsumer();
        mcs.start();
    }
}
