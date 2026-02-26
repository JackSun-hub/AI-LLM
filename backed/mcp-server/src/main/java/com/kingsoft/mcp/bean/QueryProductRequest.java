package com.kingsoft.mcp.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.kingsoft.enums.ListSortEnum;
import com.kingsoft.enums.PriceCompareEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * @Author sunjiacheng
 * @Date 2025/10/20 22:54
 * @PackageName:com.kingsoft.mcp.bean
 * @ClassName: Email
 * @Description: TODO
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QueryProductRequest {

    //required = true 默认自动填充数据，所以查询的时候建议使用false
    @ToolParam(description = "商品的Id/编号", required = false)
    private String productId;

    @ToolParam(description = "商品的名称", required = false)
    private String productName;

    @ToolParam(description = "商品的品牌名称", required = false)
    private String brand;

    @ToolParam(description = "具体商品的价格", required = false)
    private Integer price;

    @ToolParam(description = "商品的状态 (上架状态的值为1/下架状态的值为0/预售状态的值为2)", required = false)
    private Integer status;

    @ToolParam(description = "查询列表的排序", required = false)
    private ListSortEnum sortEnum;

    @ToolParam(description = "比较价格的大小", required = false)
    private PriceCompareEnum priceCompareEnum;


}
