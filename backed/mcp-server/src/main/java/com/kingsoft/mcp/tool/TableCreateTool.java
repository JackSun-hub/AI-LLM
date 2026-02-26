package com.kingsoft.mcp.tool;


import com.kingsoft.mcp.bean.CreateTableRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
/**
 * @Author sunjiacheng
 * @Date 2025/10/21 22:59
 * @PackageName:com.kingsoft.mcp.tool
 * @ClassName: TableCreateTool
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j
@Component
public class TableCreateTool {

    @Autowired
    private JdbcTemplate jdbcTemplate; // 用于执行SQL（需Spring配置）

    /**
     * 根据表结构描述创建数据库表
     * @param request 表结构请求参数（由AI解析自然语言后生成）
     * @return 创建结果
     */
    @Tool(description = "根据表名、字段列表（包含字段名、类型、长度、约束等）创建数据库表")
    public String createTable(CreateTableRequest request) {
        log.info("========调用MCP工具：createTable() ===========");
        log.info(String.format("创建表的参数为：%s", request.toString()));

        try {
            // 1. 生成CREATE TABLE SQL语句
            String createSql = generateCreateTableSql(request);
            log.info("执行建表SQL：{}", createSql);

            // 2. 执行SQL（通过JdbcTemplate执行DDL）
            jdbcTemplate.execute(createSql);
            return String.format("表【%s】创建成功", request.getTableName());
        } catch (Exception e) {
            log.error("创建表失败", e);
            return String.format("表【%s】创建失败：%s", request.getTableName(), e.getMessage());
        }
    }

    /**
     * 生成建表SQL语句
     */
    private String generateCreateTableSql(CreateTableRequest request) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS `").append(request.getTableName()).append("` (");

        List<CreateTableRequest.TableColumn> columns = request.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            CreateTableRequest.TableColumn column = columns.get(i);
            // 字段名 + 类型
            sql.append("`").append(column.getColumnName()).append("` ")
                    .append(column.getDbType());

            // 处理长度（如VARCHAR(50)）
            if (column.getLength() != null && column.getLength() > 0) {
                sql.append("(").append(column.getLength()).append(")");
            }

            // 非空约束
            if (!column.isNullable()) {
                sql.append(" NOT NULL");
            }

            // 自增（仅主键可能需要）
            if (column.isPrimaryKey() && column.isAutoIncrement()) {
                sql.append(" AUTO_INCREMENT");
            }

            // 字段注释
            if (column.getComment() != null && !column.getComment().isEmpty()) {
                sql.append(" COMMENT '").append(escapeComment(column.getComment())).append("'");
            }

            // 拼接逗号（最后一个字段不加）
            if (i != columns.size() - 1) {
                sql.append(", ");
            }
        }

        // 处理主键约束
        List<CreateTableRequest.TableColumn> primaryKeys = columns.stream()
                .filter(CreateTableRequest.TableColumn::isPrimaryKey)
                .toList();
        if (!primaryKeys.isEmpty()) {
            sql.append(", PRIMARY KEY (");
            for (int i = 0; i < primaryKeys.size(); i++) {
                sql.append("`").append(primaryKeys.get(i).getColumnName()).append("`");
                if (i != primaryKeys.size() - 1) {
                    sql.append(", ");
                }
            }
            sql.append(")");
        }

        // 表注释
        if (request.getTableComment() != null && !request.getTableComment().isEmpty()) {
            sql.append(") COMMENT = '").append(escapeComment(request.getTableComment())).append("'");
        } else {
            sql.append(")");
        }

        return sql.toString();
    }

    /**
     * 转义注释中的特殊字符（如单引号）
     */
    private String escapeComment(String comment) {
        return comment.replace("'", "\\'");
    }
}