package com.kingsoft.controller;

import com.kingsoft.service.ChatService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private ChatService chatService;

    //http://localhost:8080/hello/world
    @GetMapping("/world")
    public String world(){
        return "hello world";
    }
    @GetMapping("/chat")
    public String chat(String msg){
        return chatService.chatTest(msg);
    }

    @GetMapping("/chat/stream/response")
    public Flux<ChatResponse> chatStreamResponse(String msg,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        return chatService.streamResponse(msg);
    }

    @GetMapping("/chat/stream/str")
    public Flux<String> chatStreamStr(String msg, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        return chatService.streamStr(msg);
    }
}
