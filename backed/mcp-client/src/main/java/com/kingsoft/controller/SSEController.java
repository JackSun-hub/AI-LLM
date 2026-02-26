package com.kingsoft.controller;

import com.kingsoft.enums.SSEMsgType;
import com.kingsoft.service.ChatService;
import com.kingsoft.utils.SSEService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 11:44
 * @PackageName:com.kingsoft.controller
 * @ClassName: SSEController
 * @Description: TODO
 * @Version 1.0
 */
@RestController
@RequestMapping("/sse")
public class SSEController {
    /*
     * @Description: 前端发送连接的请求，连接SSE服务
     * @param
     * @return java.lang.String
     * @Author: sunjiacheng
     * @Date: 2025/10/14 16:25
     */
    @GetMapping(path = "/connect", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter connect(@RequestParam String userId){
        return SSEService.connect(userId);
    }

    /*
     * @Description: 前端给后端发送单个消息
     * @param userId
     * @param message
     * @return java.lang.Object
     * @Author: sunjiacheng
     * @Date: 2025/10/14 21:47
     */
    @GetMapping("sendMessage")
    public Object sendMessage(@RequestParam String userId, @RequestParam String message){
      SSEService.sendMsg(userId,message, SSEMsgType.MESSAGE);
      return "OK";
    }
    /*
     * @Description: 前端给后端发送单个消息 -add
     * @param userId
     * @param message
     * @return java.lang.Object
     * @Author: sunjiacheng
     * @Date: 2025/10/14 22:15
     */
    @GetMapping("sendMessageAdd")
    public Object sendMessageAdd(@RequestParam String userId, @RequestParam String message) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(200);
            SSEService.sendMsg(userId,message, SSEMsgType.MESSAGE);
        }
        return "OK";
    }
    /**
     * @Description: 前端给后端发送多个消息
     * @param message
     * @return java.lang.Object
     * @Author: sunjiacheng
     * @Date: 2025/10/14 21:47
     */
    @GetMapping("sendMessageAll")
    public Object sendMessageAll(@RequestParam String message){
        SSEService.sendMsgToAllUsers(message);
        return "OK";
    }
}
