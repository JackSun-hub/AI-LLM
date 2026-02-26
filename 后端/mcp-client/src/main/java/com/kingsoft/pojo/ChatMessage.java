package com.kingsoft.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 聊天消息实体类
 */
@Data
public class ChatMessage {
    
    private String id;
    
    private String conversationId;
    
    private String content;
    
    private String chatType; // user 或 bot
    
    private String botMsgId;
    
    private LocalDateTime createTime;
    
    private String time; // 格式化的时间字符串，用于前端显示
}
