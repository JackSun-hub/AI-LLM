package com.kingsoft.controller;

import com.kingsoft.bean.ChatEntity;
import com.kingsoft.service.ChatService;
import com.kingsoft.service.SearXngService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 11:44
 * @PackageName:com.kingsoft.controller
 * @ClassName: HelloController
 * @Description: TODO
 * @Version 1.0
 */
@RestController
@RequestMapping("/internet")
public class InternetController {

    @Resource
    private SearXngService searXngService;

    @Resource
    private ChatService chatService;


    @GetMapping("/test")
    public Object world(@RequestParam("query") String query){
        return searXngService.search(query);
    }

    @PostMapping("/search")
    public void search(@RequestBody ChatEntity chatEntity, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        chatService.doInternetSearch(chatEntity);
    }

}
