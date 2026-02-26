package com.kingsoft.service.impl;

import cn.hutool.json.JSONUtil;
import com.kingsoft.bean.SearXngReponse;
import com.kingsoft.bean.SearchResultXng;
import com.kingsoft.service.SearXngService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author sunjiacheng
 * @Date 2025/10/17 00:51
 * @PackageName:com.kingsoft.service.impl
 * @ClassName: SearXngServiceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SearXngServiceImpl implements SearXngService {
   @Value("${internet.websearch.searxng.url}")
    private String SEARXGN_URL;
    @Value("${internet.websearch.searxng.counts}")
    private Integer COUNTS;

    private final OkHttpClient okHttpClient;
    @Override
    public List<SearchResultXng> search(String query) {
        HttpUrl url = HttpUrl.get(SEARXGN_URL).newBuilder()
                .addQueryParameter("q", query)
                .addQueryParameter("format", "json").build();
              log.info("SearXng url: {}", url.url());
        //  构建请求
        Request request = new Request.Builder().url(url).build();
        //只try这一行代码，这一行代码报错，才能捕获异常
        try(Response response = okHttpClient.newBuilder().build().newCall(request).execute();) {
            //判断请求是否成功
           if (!response.isSuccessful()){
               throw new RuntimeException("请求失败：HTTP" + response.code());
           }
           //获取响应体
           if (response.body() != null){
               String res = response.body().string();
               return dealResult(JSONUtil.toBean(res, SearXngReponse.class).getResults());
           }
           log.error("请求失败：{}", response.message());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Collections.emptyList();
    }

    private List<SearchResultXng> dealResult(List<SearchResultXng> resultXngs) {
        List<SearchResultXng> searchResultXngs = resultXngs.subList(0, Math.min(COUNTS, resultXngs.size()))
                .parallelStream()//流并行处理 对的大数据集进行并行 处理
                .sorted(Comparator.comparingDouble(SearchResultXng::getScore).reversed())
                .limit(COUNTS).toList();
        return searchResultXngs;
    }
}
