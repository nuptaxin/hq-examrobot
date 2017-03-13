package ren.ashin.hq.examrobot.dao;

import org.apache.ibatis.annotations.Select;

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
}
