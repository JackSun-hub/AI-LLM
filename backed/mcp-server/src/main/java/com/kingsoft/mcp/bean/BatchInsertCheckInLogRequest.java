package com.kingsoft.mcp.bean;

import lombok.Data;
import java.util.List;
/**
 * @Author sunjiacheng
 * @Date 2025/10/21 23:15
 * @PackageName:com.kingsoft.mcp.bean
 * @ClassName: BatchInsertCheckInLogRequest
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class BatchInsertCheckInLogRequest {
    // 批量插入的打卡记录列表
    private List<InsertCheckInLogRequest> records; // 复用之前的单条请求类
}
