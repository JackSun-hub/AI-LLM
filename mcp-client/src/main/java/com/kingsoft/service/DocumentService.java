package com.kingsoft.service;

import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * @Author sunjiacheng
 * @Date 2025/10/15 20:57
 * @PackageName:com.kingsoft.service
 * @ClassName: DocumentService
 * @Description: TODO
 * @Version 1.0
 */
public interface DocumentService {

    /**
     * @Author sunjiacheng
     * @Date 2025/10/15 20:57
     * @PackageName:com.kingsoft.service
     * @ClassName: DocumentService
     * @Description: 加载文档并且读取数据进行保存到数据库
     * @Version 1.0
     */
    public void loadText(Resource resource, String fileName);

    public List<Document> doSearch(String question);

}
