package com.kingsoft.utils;

import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.List;

/**
 * @Author sunjiacheng
 * @Date 2025/10/15 21:19
 * @PackageName:com.kingsoft.utils
 * @ClassName: CustomTextSplitter
 * @Description: TODO
 * @Version 1.0
 */
public class CustomTextSplitter extends TextSplitter {
    @Override
    protected List<String> splitText(String text) {
        return List.of(split(text));
    }

    public String[] split(String text) {
        //空字符串换行
        return text.split("\\s*\\R\\s*\\R\\s*");
    }
}
