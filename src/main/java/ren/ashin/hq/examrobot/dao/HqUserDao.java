package ren.ashin.hq.examrobot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import ren.ashin.hq.examrobot.bean.HqTask;
import ren.ashin.hq.examrobot.bean.HqUser;

/**
 * @ClassName: HqTaskDao
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public interface HqUserDao {
    @Select("select * from hq_user t where t.id = #{id}")
    HqUser findUserById(Long id);
    @Select("select * from hq_user t where t.status = #{status}")
    List<HqUser> findUserByStatus(Long status);
    @Update("update hq_user set status = #{status}, name = #{name} where id = #{id}")
    void updateUserStatus(HqUser user);
}
