package com.kingsoft.pojo;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @Author sunjiacheng
 * @Date 2025/10/21 20:24
 * @PackageName:com.kingsoft.pojo
 * @ClassName: Product
 * @Description: TODO
 * @Version 1.0
 */
@Data
@ToString
public class Product {
    private String productId;
    private String productName;
    private String brand;
    private String description;

    private Integer price;
    private Integer stock;
    private Integer status;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
