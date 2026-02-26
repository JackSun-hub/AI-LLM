package com.kingsoft.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

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
public class SearXngReponse {
    private String query;
    private List<SearchResultXng> results;



}
