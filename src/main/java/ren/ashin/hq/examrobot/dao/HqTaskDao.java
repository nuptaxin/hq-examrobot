package ren.ashin.hq.examrobot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import ren.ashin.hq.examrobot.bean.HqTask;

/**
 * @ClassName: HqTaskDao
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public interface HqTaskDao {
    @Select("select * from hq_task t where t.status = 0")
    List<HqTask> findNotRunTask();
    @Update("update hq_task set status = #{status} where id = #{id}")
    void updateTaskStatus(HqTask hqTask);
}
