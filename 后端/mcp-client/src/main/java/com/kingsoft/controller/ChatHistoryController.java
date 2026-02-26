package com.kingsoft.controller;

import com.kingsoft.pojo.ChatMessage;
import com.kingsoft.pojo.Conversation;
import com.kingsoft.service.ChatHistoryService;
import com.kingsoft.utils.SunResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 对话历史控制器
 */
@Slf4j
@RestController
@RequestMapping("/chat/history")
public class ChatHistoryController {

    @Resource
    private ChatHistoryService chatHistoryService;

    /**
     * 获取对话列表
     */
    @GetMapping("/list")
    public SunResult getChatHistoryList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String userId) {
        
        log.info("获取对话列表: userId={}, page={}, pageSize={}", userId, page, pageSize);
        
        try {
            if (userId == null || userId.isEmpty()) {
                return SunResult.errorMsg("用户ID不能为空");
            }
            
            List<Conversation> list = chatHistoryService.getConversationList(userId, page, pageSize);
            int total = chatHistoryService.getConversationCount(userId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("total", total);
            data.put("list", list);
            
            return SunResult.ok(data);
        } catch (Exception e) {
            log.error("获取对话列表失败", e);
            return SunResult.errorMsg("获取对话列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取对话消息
     */
    @GetMapping("/messages")
    public SunResult getChatMessages(@RequestParam String conversationId) {
        log.info("获取对话消息: conversationId={}", conversationId);
        
        try {
            Conversation conversation = chatHistoryService.getConversationWithMessages(conversationId);
            if (conversation != null) {
                return SunResult.ok(conversation);
            } else {
                return SunResult.errorMsg("对话不存在");
            }
        } catch (Exception e) {
            log.error("获取对话消息失败", e);
            return SunResult.errorMsg("获取对话消息失败: " + e.getMessage());
        }
    }

    /**
     * 创建新对话
     */
    @PostMapping("/create")
    public SunResult createConversation(@RequestBody Map<String, String> params) {
        String userId = params.get("userId");
        String title = params.get("title");
        
        log.info("创建新对话: userId={}, title={}", userId, title);
        
        try {
            if (userId == null || userId.isEmpty()) {
                return SunResult.errorMsg("用户ID不能为空");
            }
            
            Conversation conversation = chatHistoryService.createConversation(userId, title);
            return SunResult.ok(conversation);
        } catch (Exception e) {
            log.error("创建对话失败", e);
            return SunResult.errorMsg("创建对话失败: " + e.getMessage());
        }
    }

    /**
     * 删除对话
     */
    @PostMapping("/delete")
    public SunResult deleteConversation(@RequestBody Map<String, String> params) {
        String conversationId = params.get("conversationId");
        
        log.info("删除对话: conversationId={}", conversationId);
        
        try {
            boolean success = chatHistoryService.deleteConversation(conversationId);
            if (success) {
                return SunResult.ok();
            } else {
                return SunResult.errorMsg("删除失败");
            }
        } catch (Exception e) {
            log.error("删除对话失败", e);
            return SunResult.errorMsg("删除对话失败: " + e.getMessage());
        }
    }

    /**
     * 重命名对话
     */
    @PostMapping("/rename")
    public SunResult renameConversation(@RequestBody Map<String, String> params) {
        String conversationId = params.get("conversationId");
        String title = params.get("title");
        
        log.info("重命名对话: conversationId={}, title={}", conversationId, title);
        
        try {
            boolean success = chatHistoryService.renameConversation(conversationId, title);
            if (success) {
                return SunResult.ok();
            } else {
                return SunResult.errorMsg("重命名失败");
            }
        } catch (Exception e) {
            log.error("重命名对话失败", e);
            return SunResult.errorMsg("重命名对话失败: " + e.getMessage());
        }
    }

    /**
     * 清空所有历史
     */
    @PostMapping("/clear")
    public SunResult clearAllHistory(@RequestParam(required = false) String userId) {
        log.info("清空所有历史: userId={}", userId);
        
        try {
            if (userId == null || userId.isEmpty()) {
                return SunResult.errorMsg("用户ID不能为空");
            }
            
            boolean success = chatHistoryService.clearAllHistory(userId);
            if (success) {
                return SunResult.ok();
            } else {
                return SunResult.errorMsg("清空失败");
            }
        } catch (Exception e) {
            log.error("清空历史失败", e);
            return SunResult.errorMsg("清空历史失败: " + e.getMessage());
        }
    }

    /**
     * 保存消息到对话
     */
    @PostMapping("/saveMessage")
    public SunResult saveMessage(@RequestBody ChatMessage message) {
        log.info("保存消息: conversationId={}, chatType={}", 
                message.getConversationId(), message.getChatType());
        
        try {
            boolean success = chatHistoryService.saveMessage(message);
            if (success) {
                return SunResult.ok();
            } else {
                return SunResult.errorMsg("保存失败");
            }
        } catch (Exception e) {
            log.error("保存消息失败", e);
            return SunResult.errorMsg("保存消息失败: " + e.getMessage());
        }
    }
}
