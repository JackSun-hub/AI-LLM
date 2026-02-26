package com.kingsoft.mcp.tool;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kingsoft.enums.ListSortEnum;
import com.kingsoft.enums.PriceCompareEnum;
import com.kingsoft.mapper.ProductMapper;
import com.kingsoft.mcp.bean.ModifyProductRequest;
import com.kingsoft.mcp.bean.ProductRequest;
import com.kingsoft.mcp.bean.QueryProductRequest;
import com.kingsoft.pojo.Product;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author sunjiacheng
 * @Date 2025/10/20 22:23
 * @PackageName:com.kingsoft.mcp.tool
 * @ClassName: ProductTool
 * @Description: TODO
 * @Version 1.0
 */
@Component
@Slf4j
public class ProductTool {
    @Resource
    private ProductMapper productMapper;


    @Tool(description = "创建/新增商品信息记录")
    public String createProduct(ProductRequest productRequest) {
        log.info("========调用MCP工具：createProduct() ===========");
        Product product = new Product();
        BeanUtils.copyProperties(productRequest,product);
        //生成12位的随机数
        product.setProductId(RandomStringUtils.randomNumeric(12));

        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        productMapper.insert(product);
        return "商品创建成功";
    }
    @Transactional
    @Tool(description = "只能根据商品的主键Id删除商品/删除商品只能根据商品的主键Id")
    public String delProduct(String productId) {
        log.info("========调用MCP工具：delProduct() ===========");
        productMapper.deleteProductById(productId);
        return "商品删除成功";
    }

    @Tool(description = "把排序（正序/倒序）转换为对应的枚举")
    public ListSortEnum getSortEnum(String sort) {
        log.info("========调用MCP工具：getSortEnum() ===========");
        if (sort.equalsIgnoreCase(ListSortEnum.ASC.value)){
            return ListSortEnum.ASC;
        } else {
            return ListSortEnum.DESC;
        }
    }

    @Tool(description = "把商品价格的比较（大于/小于/大于等于/小于等于/高于/低于/不高于/不低于）转换为对应的枚举")
    public PriceCompareEnum getPriceCompareEnum(String priceCompareEnum) {
        log.info("========调用MCP工具：getPriceCompareEnum() ===========");
        log.info(String.format("商品价格比较枚举为：%s",priceCompareEnum));
        if (priceCompareEnum.equalsIgnoreCase(PriceCompareEnum.GREATER_THAN.value)){
            return PriceCompareEnum.GREATER_THAN;
        } else if (priceCompareEnum.equalsIgnoreCase(PriceCompareEnum.LESS_THAN.value)) {
            return PriceCompareEnum.LESS_THAN;
        }else if (priceCompareEnum.equalsIgnoreCase(PriceCompareEnum.GREATER_THAN_EQUAL_TO.value)) {
            return PriceCompareEnum.GREATER_THAN_EQUAL_TO;
        }else if (priceCompareEnum.equalsIgnoreCase(PriceCompareEnum.LESS_THAN_EQUAL_TO.value)) {
            return PriceCompareEnum.LESS_THAN_EQUAL_TO;
        }else if (priceCompareEnum.equalsIgnoreCase(PriceCompareEnum.HIGHER_THAN.value)) {
            return PriceCompareEnum.HIGHER_THAN;
        }else if (priceCompareEnum.equalsIgnoreCase(PriceCompareEnum.LOWER_THAN.value)) {
            return PriceCompareEnum.LOWER_THAN;
        }else if (priceCompareEnum.equalsIgnoreCase(PriceCompareEnum.NOT_HIGHER_THAN.value)) {
            return PriceCompareEnum.NOT_HIGHER_THAN;
        }else if (priceCompareEnum.equalsIgnoreCase(PriceCompareEnum.NOT_LOWER_THAN.value)) {
            return PriceCompareEnum.NOT_LOWER_THAN;
        } else {
            return PriceCompareEnum.EQUAL_TO;
        }
    }

    @Tool(description = "根据条件查询商品（product）信息")
    public List<Product> queryProductListByCondition(QueryProductRequest queryProductRequest) {
        log.info("========调用MCP工具：queryProductListByCondition() ===========");
        log.info(String.format("查询商品列表的参数为：%s",queryProductRequest.toString()));
        String productId = queryProductRequest.getProductId();
        String productName = queryProductRequest.getProductName();
        String brand = queryProductRequest.getBrand();

        Integer status = queryProductRequest.getStatus();
        ListSortEnum sortEnum = queryProductRequest.getSortEnum();

        Integer price = queryProductRequest.getPrice();
        PriceCompareEnum priceCompareEnum = queryProductRequest.getPriceCompareEnum();

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(productId)){
            queryWrapper.eq("product_id",productId);
        }
        if (StringUtils.isNotBlank(productName)){
            queryWrapper.like("product_name",productName);
        }
        if (StringUtils.isNotBlank(brand)){
            queryWrapper.like("brand",brand);
        }
        if (status != null){
            queryWrapper.eq("status",status);
        }

        if (price != null && priceCompareEnum != null){
            if (priceCompareEnum.type.equals(PriceCompareEnum.GREATER_THAN.type)){
                queryWrapper.gt("price",price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.LESS_THAN.type)) {
                queryWrapper.lt("price",price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.GREATER_THAN_EQUAL_TO.type)) {
                queryWrapper.ge("price",price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.LESS_THAN_EQUAL_TO.type)) {
                queryWrapper.le("price",price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.HIGHER_THAN.type)) {
                queryWrapper.gt("price",price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.LOWER_THAN.type)) {
                queryWrapper.lt("price",price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.NOT_HIGHER_THAN.type)) {
                queryWrapper.le("price",price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.NOT_LOWER_THAN.type)) {
                queryWrapper.ge("price",price);
            } else {
                queryWrapper.eq("price",price);
            }
        }
        // TODO 也可以根据商品Id ，品牌等进行排序，一样写出枚举进行判断
        if (sortEnum != null && sortEnum.type.equals(ListSortEnum.ASC.type)){
            queryWrapper.orderByAsc("price");
        }
        if (sortEnum != null && sortEnum.type.equals(ListSortEnum.DESC.type)){
            queryWrapper.orderByDesc("price");
        }
        List<Product> productList = productMapper.selectList(queryWrapper);
        return productList;
    }


    @Tool(description = "根据商品的ID/编号修改商品的信息")
    public String modifyProduct(ModifyProductRequest modifyProductRequest) {
        log.info("========调用MCP工具：modifyProduct() ===========");
        log.info(String.format("修改商品列表的参数为：%s",modifyProductRequest.toString()));
        Product product = new Product();
        BeanUtils.copyProperties(modifyProductRequest,product);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id",modifyProductRequest.getProductId());
        int update = productMapper.update(product, queryWrapper);
        if (update <= 0){
            return "商品信息更新失败 ｜ 商品不存在";
        }
        return "商品信息更新成功";
    }
}
