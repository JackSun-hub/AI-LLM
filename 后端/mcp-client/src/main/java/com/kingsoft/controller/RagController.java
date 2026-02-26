package com.kingsoft.controller;

import com.kingsoft.bean.ChatEntity;
import com.kingsoft.service.ChatService;
import com.kingsoft.service.DocumentService;
import com.kingsoft.utils.SunResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 11:44
 * @PackageName:com.kingsoft.controller
 * @ClassName: HelloController
 * @Description: TODO
 * @Version 1.0
 */
@RestController
@RequestMapping("/rag")
public class RagController {

    @Resource
    private DocumentService documentService;

    @Resource
    private ChatService chatService;

    @PostMapping("/uploadRagDoc")
    public SunResult uploadRagDoc(@RequestParam("file") MultipartFile file){
        //文件名称
        String originalFilename = file.getOriginalFilename();
        documentService.loadText(file.getResource(),originalFilename);
        return SunResult.ok();
    }

    @GetMapping("/doSearch")
    public SunResult doSearch(@RequestParam String question){
        return SunResult.ok(documentService.doSearch(question));
    }

    @PostMapping("/search")
    public void search(@RequestBody ChatEntity chatEntity,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        List<Document> documents = documentService.doSearch(chatEntity.getMessage());
        chatService.doChatRagSearch(chatEntity, documents);
    }

}
