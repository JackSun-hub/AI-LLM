package com.kingsoft.mapper;

import com.kingsoft.pojo.User;
import org.apache.ibatis.annotations.*;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);
    
    /**
     * 根据用户ID查询用户
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") String id);
    
    /**
     * 插入新用户
     */
    @Insert("INSERT INTO user (id, username, password, email, avatar, is_guest, status, create_time, update_time) " +
            "VALUES (#{id}, #{username}, #{password}, #{email}, #{avatar}, #{isGuest}, #{status}, #{createTime}, #{updateTime})")
    int insert(User user);
    
    /**
     * 更新用户信息
     */
    @Update("UPDATE user SET username=#{username}, email=#{email}, avatar=#{avatar}, " +
            "status=#{status}, update_time=#{updateTime} WHERE id=#{id}")
    int update(User user);
    
    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int countByUsername(@Param("username") String username);
}
