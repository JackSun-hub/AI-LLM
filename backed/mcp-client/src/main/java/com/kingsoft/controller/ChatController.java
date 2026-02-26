package com.kingsoft.controller;

import com.kingsoft.bean.ChatEntity;
import com.kingsoft.service.ChatService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 11:44
 * @PackageName:com.kingsoft.controller
 * @ClassName: HelloController
 * @Description: TODO
 * @Version 1.0
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private ChatService chatService;

    @PostMapping("/doChat")
    public void doChat(@RequestBody ChatEntity chatEntity){
         chatService.doChat(chatEntity);
    }

}
