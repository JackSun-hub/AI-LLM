package com.kingsoft.service;

import com.kingsoft.pojo.ChatMessage;
import com.kingsoft.pojo.Conversation;

import java.util.List;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 对话历史服务接口
 */
public interface ChatHistoryService {
    
    /**
     * 获取用户的对话列表
     */
    List<Conversation> getConversationList(String userId, Integer page, Integer pageSize);
    
    /**
     * 获取用户的对话总数
     */
    int getConversationCount(String userId);
    
    /**
     * 获取对话及其消息
     */
    Conversation getConversationWithMessages(String conversationId);
    
    /**
     * 创建新对话
     */
    Conversation createConversation(String userId, String title);
    
    /**
     * 删除对话
     */
    boolean deleteConversation(String conversationId);
    
    /**
     * 重命名对话
     */
    boolean renameConversation(String conversationId, String title);
    
    /**
     * 清空用户所有历史
     */
    boolean clearAllHistory(String userId);
    
    /**
     * 保存消息到对话
     */
    boolean saveMessage(ChatMessage message);
    
    /**
     * 获取对话的所有消息
     */
    List<ChatMessage> getMessages(String conversationId);
}
