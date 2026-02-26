package com.kingsoft.pojo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 对话实体类
 */
@Data
public class Conversation {
    
    private String id;
    
    private String userId;
    
    private String title;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Integer messageCount;
    
    private Integer status; // 0-正常 1-已删除
    
    // 对话消息列表（不存数据库，用于返回）
    private List<ChatMessage> messages;
}
