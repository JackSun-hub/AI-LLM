package com.kingsoft.mcp.bean;

import lombok.Data;

import java.util.Date;

/**
 * @Author sunjiacheng
 * @Date 2025/10/21 23:11
 * @PackageName:com.kingsoft.mcp.bean
 * @ClassName: InsertCheckInLogRequest
 * @Description: TODO
 * @Version 1.0
 */

@Data
public class InsertCheckInLogRequest {
    private Integer id; // 记录ID（自增可省略，由数据库生成）
    private String employeeId; // 员工ID
    private String userId; // 员工账号
    private Date checkTime; // 打卡时间（DATETIME）
    private String location; // 打卡地点
    private Date checkDate; // 打卡日期（DATE）
}
