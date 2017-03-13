package ren.ashin.hq.examrobot.service;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

import ren.ashin.hq.examrobot.bean.HqTask;

/**
 * @ClassName: TaskQueueService
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
@Component
public class TaskQueueService {
    private LinkedBlockingQueue<HqTask> queue = new LinkedBlockingQueue<HqTask>();
    private int taskSize = 0;
    public LinkedBlockingQueue<HqTask> getQueue() {
        return queue;
    }
    public void setQueue(LinkedBlockingQueue<HqTask> queue) {
        this.queue = queue;
    }
    public int getTaskSize() {
        return taskSize;
    }
    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    
}
