package com.kingsoft.mapper;

import com.kingsoft.pojo.ChatMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 聊天消息Mapper
 */
@Mapper
public interface ChatMessageMapper {
    
    /**
     * 插入消息
     */
    @Insert("INSERT INTO chat_message(id, conversation_id, content, chat_type, bot_msg_id, create_time) " +
            "VALUES(#{id}, #{conversationId}, #{content}, #{chatType}, #{botMsgId}, #{createTime})")
    int insert(ChatMessage message);
    
    /**
     * 根据对话ID查询消息列表
     */
    @Select("SELECT * FROM chat_message WHERE conversation_id = #{conversationId} ORDER BY create_time ASC")
    List<ChatMessage> selectByConversationId(String conversationId);
    
    /**
     * 根据对话ID统计消息数量
     */
    @Select("SELECT COUNT(*) FROM chat_message WHERE conversation_id = #{conversationId}")
    int countByConversationId(String conversationId);
    
    /**
     * 根据对话ID删除所有消息
     */
    @Delete("DELETE FROM chat_message WHERE conversation_id = #{conversationId}")
    int deleteByConversationId(String conversationId);
    
    /**
     * 批量删除多个对话的消息
     */
    @Delete("<script>" +
            "DELETE FROM chat_message WHERE conversation_id IN " +
            "<foreach collection='conversationIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int deleteByConversationIds(@Param("conversationIds") List<String> conversationIds);
}
