package com.kingsoft.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author sunjiacheng
 * @Date 2025/10/17 00:47
 * @PackageName:com.kingsoft.bean
 * @ClassName: SearchResultXng
 * @Description: TODO
 * @Version 1.0
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultXng {
    private String title;
    private String content;
    private String url;
    private Double score;



}
