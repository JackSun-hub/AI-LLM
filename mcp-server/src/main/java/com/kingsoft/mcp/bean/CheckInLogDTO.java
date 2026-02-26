package com.kingsoft.mcp.bean;

import lombok.Data;

/**
 * 打卡记录DTO - 用于MCP工具返回，时间字段已格式化为字符串
 * 避免AI在流式输出时处理Date对象导致的数字显示问题
 */
@Data
public class CheckInLogDTO {
    private Integer id;
    private String employeeId;
    private String userId;
    private String checkTime;  // 格式化后的时间字符串 yyyy-MM-dd HH:mm:ss
    private String location;
    private String checkDate;  // 格式化后的日期字符串 yyyy-MM-dd
}
