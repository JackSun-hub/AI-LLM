package com.kingsoft.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kingsoft.pojo.Product;
import org.apache.ibatis.annotations.Delete;

/**
 * @Author sunjiacheng
 * @Date 2025/10/21 20:28
 * @PackageName:com.kingsoft.mapper
 * @ClassName: ProductMapper
 * @Description: TODO
 * @Version 1.0
 */
public interface ProductMapper extends BaseMapper<Product> {
    @Delete("DELETE FROM product WHERE product_id = #{productId}") // 表名和字段名替换为实际值
    int deleteProductById(String productId);
}