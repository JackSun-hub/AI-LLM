package com.kingsoft.service.impl;

import com.kingsoft.service.DocumentService;
import com.kingsoft.utils.CustomTextSplitter;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author sunjiacheng
 * @Date 2025/10/15 20:57
 * @PackageName:com.kingsoft.service.impl
 * @ClassName: DocumentServiceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private  final RedisVectorStore redisVectorStore;
    @Override
    public void loadText(Resource resource, String fileName) {
        //读取加载文件
        TikaDocumentReader textReader = new TikaDocumentReader(resource);
       // textReader.getCustomMetadata().put("fileName", fileName);
        List<Document> documents = textReader.get();
        // 为每个文档添加文件名元数据
        for (Document document : documents) {
            document.getMetadata().put("fileName", fileName);
        }
        //默认的文本切分器
//        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
//        List<Document> list = tokenTextSplitter.apply(documents);
        CustomTextSplitter tokenTextSplitter = new CustomTextSplitter();
        List<Document> list = tokenTextSplitter.apply(documents);
        //向量存储
        redisVectorStore.add(list);
        System.out.println(list);
    }

    @Override
    public List<Document> doSearch(String question) {
        return redisVectorStore.similaritySearch(question);
    }
}
