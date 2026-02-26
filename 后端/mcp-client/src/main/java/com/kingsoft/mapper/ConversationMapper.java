package com.kingsoft.mapper;

import com.kingsoft.pojo.Conversation;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 对话Mapper
 */
@Mapper
public interface ConversationMapper {
    
    /**
     * 插入对话
     */
    @Insert("INSERT INTO conversation(id, user_id, title, message_count, status, create_time, update_time) " +
            "VALUES(#{id}, #{userId}, #{title}, #{messageCount}, #{status}, #{createTime}, #{updateTime})")
    int insert(Conversation conversation);
    
    /**
     * 根据ID查询对话
     */
    @Select("SELECT * FROM conversation WHERE id = #{id}")
    Conversation selectById(String id);
    
    /**
     * 根据用户ID查询对话列表（分页）
     */
    @Select("SELECT * FROM conversation WHERE user_id = #{userId} AND status = 0 " +
            "ORDER BY update_time DESC LIMIT #{offset}, #{pageSize}")
    List<Conversation> selectByUserId(@Param("userId") String userId, 
                                      @Param("offset") int offset, 
                                      @Param("pageSize") int pageSize);
    
    /**
     * 统计用户的对话数量
     */
    @Select("SELECT COUNT(*) FROM conversation WHERE user_id = #{userId} AND status = 0")
    int countByUserId(String userId);
    
    /**
     * 更新对话
     */
    @Update("UPDATE conversation SET title = #{title}, message_count = #{messageCount}, " +
            "update_time = #{updateTime} WHERE id = #{id}")
    int update(Conversation conversation);
    
    /**
     * 更新对话标题
     */
    @Update("UPDATE conversation SET title = #{title}, update_time = NOW() WHERE id = #{id}")
    int updateTitle(@Param("id") String id, @Param("title") String title);
    
    /**
     * 更新消息数量
     */
    @Update("UPDATE conversation SET message_count = #{messageCount}, update_time = NOW() WHERE id = #{id}")
    int updateMessageCount(@Param("id") String id, @Param("messageCount") int messageCount);
    
    /**
     * 软删除对话
     */
    @Update("UPDATE conversation SET status = 1, update_time = NOW() WHERE id = #{id}")
    int deleteById(String id);
    
    /**
     * 批量软删除用户的所有对话
     */
    @Update("UPDATE conversation SET status = 1, update_time = NOW() WHERE user_id = #{userId}")
    int deleteByUserId(String userId);
}
