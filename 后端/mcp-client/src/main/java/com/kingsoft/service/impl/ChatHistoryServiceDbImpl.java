package com.kingsoft.service.impl;

import com.kingsoft.mapper.ChatMessageMapper;
import com.kingsoft.mapper.ConversationMapper;
import com.kingsoft.pojo.ChatMessage;
import com.kingsoft.pojo.Conversation;
import com.kingsoft.service.ChatHistoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 对话历史服务实现类（数据库存储版本）
 */
@Slf4j
@Service
@Primary  // 设置为主要实现，优先使用数据库版本
public class ChatHistoryServiceDbImpl implements ChatHistoryService {
    
    @Resource
    private ConversationMapper conversationMapper;
    
    @Resource
    private ChatMessageMapper chatMessageMapper;
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    @Override
    public List<Conversation> getConversationList(String userId, Integer page, Integer pageSize) {
        log.info("从数据库获取对话列表: userId={}, page={}, pageSize={}", userId, page, pageSize);
        
        int offset = (page - 1) * pageSize;
        List<Conversation> list = conversationMapper.selectByUserId(userId, offset, pageSize);
        
        return list != null ? list : new ArrayList<>();
    }
    
    @Override
    public int getConversationCount(String userId) {
        return conversationMapper.countByUserId(userId);
    }
    
    @Override
    public Conversation getConversationWithMessages(String conversationId) {
        log.info("从数据库获取对话及消息: conversationId={}", conversationId);
        
        Conversation conversation = conversationMapper.selectById(conversationId);
        if (conversation != null && conversation.getStatus() == 0) {
            List<ChatMessage> messages = chatMessageMapper.selectByConversationId(conversationId);
            
            // 格式化消息时间
            if (messages != null) {
                messages.forEach(msg -> {
                    if (msg.getCreateTime() != null) {
                        msg.setTime(msg.getCreateTime().format(TIME_FORMATTER));
                    }
                });
            }
            
            conversation.setMessages(messages);
            return conversation;
        }
        return null;
    }
    
    @Override
    @Transactional
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
        
        int result = conversationMapper.insert(conversation);
        
        if (result > 0) {
            log.info("创建对话成功: conversationId={}, userId={}, title={}", 
                    conversationId, userId, conversation.getTitle());
            return conversation;
        } else {
            log.error("创建对话失败: userId={}, title={}", userId, title);
            return null;
        }
    }
    
    @Override
    @Transactional
    public boolean deleteConversation(String conversationId) {
        log.info("删除对话: conversationId={}", conversationId);
        
        // 软删除对话
        int result = conversationMapper.deleteById(conversationId);
        
        if (result > 0) {
            log.info("删除对话成功: conversationId={}", conversationId);
            return true;
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean renameConversation(String conversationId, String title) {
        log.info("重命名对话: conversationId={}, newTitle={}", conversationId, title);
        
        int result = conversationMapper.updateTitle(conversationId, title);
        
        if (result > 0) {
            log.info("重命名对话成功: conversationId={}, newTitle={}", conversationId, title);
            return true;
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean clearAllHistory(String userId) {
        log.info("清空用户所有历史: userId={}", userId);
        
        int result = conversationMapper.deleteByUserId(userId);
        
        if (result > 0) {
            log.info("清空用户所有历史成功: userId={}, 删除数量={}", userId, result);
            return true;
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean saveMessage(ChatMessage message) {
        String conversationId = message.getConversationId();
        
        log.info("保存消息到数据库: conversationId={}, chatType={}", conversationId, message.getChatType());
        
        // 生成消息ID
        if (message.getId() == null || message.getId().isEmpty()) {
            message.setId("msg_" + UUID.randomUUID().toString().replace("-", ""));
        }
        
        // 设置创建时间
        if (message.getCreateTime() == null) {
            message.setCreateTime(LocalDateTime.now());
        }
        
        // 保存消息到数据库
        int result = chatMessageMapper.insert(message);
        
        if (result > 0) {
            // 更新对话信息
            Conversation conversation = conversationMapper.selectById(conversationId);
            if (conversation != null) {
                int messageCount = chatMessageMapper.countByConversationId(conversationId);
                conversationMapper.updateMessageCount(conversationId, messageCount);
                
                // 如果是第一条用户消息，自动更新对话标题
                if (messageCount == 1 && "user".equals(message.getChatType())) {
                    String content = message.getContent();
                    String autoTitle = content.length() > 20 ? content.substring(0, 20) + "..." : content;
                    conversationMapper.updateTitle(conversationId, autoTitle);
                }
            }
            
            log.info("保存消息成功: conversationId={}, messageId={}, chatType={}", 
                    conversationId, message.getId(), message.getChatType());
            return true;
        } else {
            log.error("保存消息失败: conversationId={}, chatType={}", conversationId, message.getChatType());
            return false;
        }
    }
    
    @Override
    public List<ChatMessage> getMessages(String conversationId) {
        log.info("从数据库获取消息列表: conversationId={}", conversationId);
        
        List<ChatMessage> messages = chatMessageMapper.selectByConversationId(conversationId);
        
        // 格式化消息时间
        if (messages != null) {
            messages.forEach(msg -> {
                if (msg.getCreateTime() != null) {
                    msg.setTime(msg.getCreateTime().format(TIME_FORMATTER));
                }
            });
        }
        
        return messages != null ? messages : new ArrayList<>();
    }
}
