package com.kingsoft.service;

import com.kingsoft.bean.ChatEntity;
import com.kingsoft.bean.SearchResultXng;
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
public interface SearXngService {
    /**
     * @param query
     * @return java.util.List<com.kingsoft.bean.SearchResultXng>
     * @Description: 调用本地搜索引擎，searXNG 进行搜索
     * @Author sunjiacheng
     * @Date 2025/10/14 12:24
     */
    public List<SearchResultXng> search(String query);

}
