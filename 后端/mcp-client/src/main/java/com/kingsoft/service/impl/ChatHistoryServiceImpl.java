package com.kingsoft.service.impl;

import com.kingsoft.pojo.ChatMessage;
import com.kingsoft.pojo.Conversation;
import com.kingsoft.service.ChatHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 对话历史服务实现类（内存存储版本，实际项目应使用数据库）
 */
@Slf4j
@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {
    
    // 使用内存存储对话和消息（实际项目应使用数据库）
    private static final Map<String, Conversation> conversationMap = new ConcurrentHashMap<>();
    private static final Map<String, List<ChatMessage>> messageMap = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> userConversationsMap = new ConcurrentHashMap<>();
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    @Override
    public List<Conversation> getConversationList(String userId, Integer page, Integer pageSize) {
        List<String> conversationIds = userConversationsMap.getOrDefault(userId, new ArrayList<>());
        
        // 分页处理
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, conversationIds.size());
        
        if (start >= conversationIds.size()) {
            return new ArrayList<>();
        }
        
        List<String> pageIds = conversationIds.subList(start, end);
        
        return pageIds.stream()
                .map(conversationMap::get)
                .filter(Objects::nonNull)
                .filter(c -> c.getStatus() == 0)
                .sorted((c1, c2) -> c2.getUpdateTime().compareTo(c1.getUpdateTime()))
                .collect(Collectors.toList());
    }
    
    @Override
    public int getConversationCount(String userId) {
        List<String> conversationIds = userConversationsMap.getOrDefault(userId, new ArrayList<>());
        return (int) conversationIds.stream()
                .map(conversationMap::get)
                .filter(Objects::nonNull)
                .filter(c -> c.getStatus() == 0)
                .count();
    }
    
    @Override
    public Conversation getConversationWithMessages(String conversationId) {
        Conversation conversation = conversationMap.get(conversationId);
        if (conversation != null && conversation.getStatus() == 0) {
            List<ChatMessage> messages = messageMap.getOrDefault(conversationId, new ArrayList<>());
            conversation.setMessages(messages);
            return conversation;
        }
        return null;
    }
    
    @Override
    public Conversation createConversation(String userId, String title) {
        String conversationId = "conv_" + UUID.randomUUID().toString().replace("-", "");
        
        Conversation conversation = new Conversation();
        conversation.setId(conversationId);
        conversation.setUserId(userId);
        conversation.setTitle(title != null && !title.isEmpty() ? title : "新对话");
        conversation.setCreateTime(LocalDateTime.now());
        conversation.setUpdateTime(LocalDateTime.now());
        conversation.setMessageCount(0);
        conversation.setStatus(0);
        
        // 保存对话
        conversationMap.put(conversationId, conversation);
        
        // 添加到用户的对话列表
        userConversationsMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(0, conversationId);
        
        // 初始化消息列表
        messageMap.put(conversationId, new ArrayList<>());
        
        log.info("创建对话成功: conversationId={}, userId={}, title={}", 
                conversationId, userId, conversation.getTitle());
        
        return conversation;
    }
    
    @Override
    public boolean deleteConversation(String conversationId) {
        Conversation conversation = conversationMap.get(conversationId);
        if (conversation != null) {
            // 软删除
            conversation.setStatus(1);
            conversation.setUpdateTime(LocalDateTime.now());
            
            log.info("删除对话成功: conversationId={}", conversationId);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean renameConversation(String conversationId, String title) {
        Conversation conversation = conversationMap.get(conversationId);
        if (conversation != null && conversation.getStatus() == 0) {
            conversation.setTitle(title);
            conversation.setUpdateTime(LocalDateTime.now());
            
            log.info("重命名对话成功: conversationId={}, newTitle={}", conversationId, title);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean clearAllHistory(String userId) {
        List<String> conversationIds = userConversationsMap.get(userId);
        if (conversationIds != null) {
            for (String conversationId : conversationIds) {
                Conversation conversation = conversationMap.get(conversationId);
                if (conversation != null) {
                    conversation.setStatus(1);
                }
            }
            log.info("清空用户所有历史成功: userId={}", userId);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean saveMessage(ChatMessage message) {
        String conversationId = message.getConversationId();
        
        // 生成消息ID
        if (message.getId() == null || message.getId().isEmpty()) {
            message.setId("msg_" + UUID.randomUUID().toString().replace("-", ""));
        }
        
        // 设置创建时间
        if (message.getCreateTime() == null) {
            message.setCreateTime(LocalDateTime.now());
        }
        
        // 格式化时间字符串
        if (message.getTime() == null || message.getTime().isEmpty()) {
            message.setTime(message.getCreateTime().format(TIME_FORMATTER));
        }
        
        // 保存消息
        List<ChatMessage> messages = messageMap.computeIfAbsent(conversationId, k -> new ArrayList<>());
        messages.add(message);
        
        // 更新对话信息
        Conversation conversation = conversationMap.get(conversationId);
        if (conversation != null) {
            conversation.setMessageCount(messages.size());
            conversation.setUpdateTime(LocalDateTime.now());
            
            // 如果是第一条用户消息，自动更新对话标题
            if (messages.size() == 1 && "user".equals(message.getChatType())) {
                String content = message.getContent();
                String autoTitle = content.length() > 20 ? content.substring(0, 20) + "..." : content;
                conversation.setTitle(autoTitle);
            }
        }
        
        log.info("保存消息成功: conversationId={}, messageId={}, chatType={}", 
                conversationId, message.getId(), message.getChatType());
        
        return true;
    }
    
    @Override
    public List<ChatMessage> getMessages(String conversationId) {
        return messageMap.getOrDefault(conversationId, new ArrayList<>());
    }
}
