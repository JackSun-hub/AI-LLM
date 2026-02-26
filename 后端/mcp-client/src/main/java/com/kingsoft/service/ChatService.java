package com.kingsoft.service;

import com.kingsoft.bean.ChatEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 12:24
 * @PackageName:com.kingsoft.service
 * @ClassName: ChatService
 * @Description: TODO
 * @Version 1.0
 */
public interface ChatService {
    /*
     * @Description: 测试大模型聊天接口
     * @param prompt
     * @return java.lang.String
     * @Author: sunjiacheng
     * @Date: 2025/10/14 12:25
     */
    public String chatTest(String prompt);

    /*
     * @Description: 流适响应回复 -JSON格式
     * @param prompt
     * @return Flux<ChatResponse>
     * @Author: sunjiacheng
     * @Date: 2025/10/14 14:19
     */
    public Flux<ChatResponse> streamResponse(String prompt);

    /*
     * @Description: 流适响应回复 -字符串格式
     * @param prompt
     * @return reactor.core.publisher.Flux<java.lang.String>
     * @Author: sunjiacheng
     * @Date: 2025/10/14 14:27
     */
    public Flux<String> streamStr(String prompt);

    /*
     * @Description: 和大模型交互
     * @param chatEntity
     * @return reactor.core.publisher.Flux<java.lang.String>
     * @Author: sunjiacheng
     * @Date: 2025/10/14 22:39
     */
    public void doChat(ChatEntity chatEntity);
    /*
     * @Description: Rag知识库检索汇总给大模型输出
     * @param chatEntity
     * @param documents
     * @return void
     * @Author: sunjiacheng
     * @Date: 2025/10/15 22:37
     */
    public void doChatRagSearch(ChatEntity chatEntity, List<Document> documents);

    /*
     * @Description: 基于searXNG的实时联网搜索
     * @param chatEntity
     * @return void
     * @Author: sunjiacheng
     * @Date: 2025/10/17 01:47
     */
    public void doInternetSearch(ChatEntity chatEntity);
}
