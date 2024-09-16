package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户信息
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 插入用户的信息
     * @param user
     */
    void insert(User user);

    /**
     * 根据id查询用户信息
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{id}")
    User getById(Long userId);

    /**
     * 查询统计用户总数量
     * @param map
     * @return
     */
    Integer getSumBymap(Map map);

    /**
     * 查询新增的用户数量
     * @param map
     * @return
     */
    Integer getNewBymap(Map map);
}
