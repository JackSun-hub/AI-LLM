package com.kingsoft.mcp.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class ProductRequest {


    @ToolParam(description = "商品的名称")
    private String productName;

    @ToolParam(description = "商品的品牌名称")
    private String brand;

    @ToolParam(description = "商品的描述(可以为空)")
    private String description;

    @ToolParam(description = "商品的价格")
    private Integer price;

    @ToolParam(description = "商品的库存")
    private Integer stock;

    @ToolParam(description = "商品的状态 (上架状态的值为1/下架状态的值为0/预售状态的值为2)")
    private Integer status;
}
