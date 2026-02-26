package com.kingsoft.mcp.bean;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.Date;
/**
 * @Author sunjiacheng
 * @Date 2025/10/21 23:35
 * @PackageName:com.kingsoft.mcp.bean
 * @ClassName: QueryCheckInLogRequest
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class QueryCheckInLogRequest {
    @JsonProperty("employee_id")
    @ToolParam(description = "员工ID", required = false)
    private String employeeId; // 员工ID（可选，如“查询EMP001的记录”
    @ToolParam(description = "打卡地点筛选", required = false)
    private String location; // 打卡地点（可选，如“查询研发部的记录”）

    @ToolParam(description = "打卡日期（checkDate，格式yyyy-MM-dd）", required = false)
    private String checkDate; // 打卡日期（精确到年月日，可选，如“2025-10-21”）

}