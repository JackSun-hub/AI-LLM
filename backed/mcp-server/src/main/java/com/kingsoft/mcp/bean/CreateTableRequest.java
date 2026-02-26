package com.kingsoft.mcp.bean;

import lombok.Data;
import java.util.List;
/**
 * @Author sunjiacheng
 * @Date 2025/10/21 23:00
 * @PackageName:com.kingsoft.mcp.bean
 * @ClassName: CreateTableRequest
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class CreateTableRequest {
    private String tableName; // 表名
    private String tableComment; // 表注释
    private List<TableColumn> columns; // 字段列表

    // 内部类：字段信息
    @Data
    public static class TableColumn {
        private String columnName; // 字段名
        private String dbType; // 数据库类型（如VARCHAR、INT、DATETIME）
        private Integer length; // 长度（如VARCHAR(50)的50，无长度可不填）
        private boolean nullable; // 是否允许为NULL
        private String comment; // 字段注释
        private boolean primaryKey; // 是否为主键
        private boolean autoIncrement; // 是否自增（针对主键）
    }
}
