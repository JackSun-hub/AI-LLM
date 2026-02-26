package com.kingsoft.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.kingsoft.enums.SSEMsgType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.Disposable;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 15:59
 * @PackageName:com.kingsoft.utils
 * @ClassName: SSEService
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j
public class SSEService {
    //存放所有的用户
    private static Map<String, SseEmitter> sseClients = new ConcurrentHashMap<>();
    
    //存放所有用户的流订阅
    private static Map<String, Disposable> userSubscriptions = new ConcurrentHashMap<>();
    
    //存放用户当前的conversationId
    private static Map<String, String> userConversations = new ConcurrentHashMap<>();
    
    //ChatMemory实例（需要注入）
    private static ChatMemory chatMemory;
    
    /**
     * 设置ChatMemory实例
     */
    public static void setChatMemory(ChatMemory memory) {
        chatMemory = memory;
    }
    
    /**
     * 检查用户SSE连接是否存在
     */
    public static boolean isConnected(String userId) {
        return sseClients.containsKey(userId);
    }
    
    /**
     * 保存用户的流订阅和conversationId
     */
    public static void saveSubscription(String userId, String conversationId, Disposable subscription) {
        // 如果已有旧的订阅，先取消
        cancelSubscription(userId);
        userSubscriptions.put(userId, subscription);
        userConversations.put(userId, conversationId);
        log.info("保存用户 {} 的流订阅，conversationId: {}", userId, conversationId);
    }
    
    /**
     * 取消用户的流订阅，并清除对话记忆
     */
    public static void cancelSubscription(String userId) {
        Disposable oldSubscription = userSubscriptions.remove(userId);
        String conversationId = userConversations.remove(userId);
        
        if (oldSubscription != null && !oldSubscription.isDisposed()) {
            oldSubscription.dispose();
            log.info("取消用户 {} 的旧流订阅", userId);
        }
        
        // 清除该对话的记忆
        if (conversationId != null && chatMemory != null) {
            chatMemory.clear(conversationId);
            log.info("清除conversationId {} 的对话记忆", conversationId);
        }
    }

    /*
     * @Description: 创建sse连接
     * @param userId
     * @return org.springframework.web.servlet.mvc.method.annotation.SseEmitter
     * @Author: sunjiacheng
     * @Date: 2025/10/14 16:18
     */
    public static SseEmitter connect(String userId) {
        //设置超时时间，0表示无超时，就是永不过期，默认30秒，超时未完成会抛出异常
        SseEmitter sseEmitter = new SseEmitter(0L);
        //注册回调方法，当有数据发送时，会调用这个方法
        sseEmitter.onTimeout(timeoutCallback(userId));
        sseEmitter.onCompletion(completionCallback(userId));
        sseEmitter.onError(errorCallback(userId));
        sseClients.put(userId, sseEmitter);

        log.info("SSE连接创建成功，连接的用户ID: {}",userId);
        return sseEmitter  ;
    }

    public static void sendMsg(String userId, String message,SSEMsgType  msgType) {
         if (CollectionUtil.isEmpty(sseClients)){
           return;
         }
         if (sseClients.containsKey(userId)){
             SseEmitter sseEmitter = sseClients.get(userId);
             sendEmitterMessage(sseEmitter,userId,message,msgType);
         }
    }

    public static void sendMsgToAllUsers(String message) {
        if (CollectionUtil.isEmpty(sseClients)){
            return;
        }
        sseClients.forEach((userId, sseEmitter) -> {
                    sendEmitterMessage(sseEmitter,userId,message,SSEMsgType.MESSAGE);
                }
        );
    }


    /**
     * @Description: 发送sse消息
     * @param sseEmitter
     * @param userId
     * @param message
     * @param msgType
     * @return void
     * @Author: sunjiacheng
     * @Date: 2025/10/14 16:19
     */
    private static void sendEmitterMessage(SseEmitter sseEmitter,
                                          String userId,
                                          String  message,
                                          SSEMsgType  msgType) {
        SseEmitter.SseEventBuilder msgEvent = sseEmitter.event()
                .id(userId)
                .data(message)
                .name(msgType.type);

        try {
            sseEmitter.send(msgEvent);
        } catch (IOException e) {
            log.error("SSE异常...{}",e.getMessage());
            remove(userId);
            //throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 错误回调
     * @param userId
     * @return java.util.function.Consumer<java.lang.Throwable>
     * @Author: sunjiacheng
     * @Date: 2025/10/14 16:19
     */
    public static Consumer<Throwable> errorCallback (String userId) {
        return Throwable -> {
            log.error("SSE连接异常... ");
            //移除用户
            remove(userId);
        };
    }
    /**
     * @Description: 超时回调
     * @param userId
     * @return java.lang.Runnable
     * @Author: sunjiacheng
     * @Date: 2025/10/14 16:19
     */
    public static Runnable timeoutCallback(String userId) {
        return () -> {
            log.info("SSE连接超时... ");
            //移除用户
            remove(userId);
        };
    }

    /**
     * @Description: 完成回调
     * @param userId
     * @return java.lang.Runnable
     * @Author: sunjiacheng
     * @Date: 2025/10/14 16:19
     */
    public static Runnable completionCallback (String userId) {
        return () -> {
            log.info("SSE完成... ");

            //移除用户
            remove(userId);
        };
    } 

    /**
     * @Description: 移除用户
     * @param userId
     * @return void
     * @Author: sunjiacheng
     * @Date: 2025/10/14 16:19
     */
    public static void remove(String userId) {
        //取消流订阅（会自动清除对话记忆）
        cancelSubscription(userId);
        //移除用户
        sseClients.remove(userId);
        log.info("SSE连接移除，移除的用户ID为：{}", userId);
    }
}
