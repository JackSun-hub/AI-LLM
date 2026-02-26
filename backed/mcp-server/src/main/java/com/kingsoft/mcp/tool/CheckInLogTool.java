package com.kingsoft.mcp.tool;


import com.kingsoft.mapper.CheckInLogMapper;
import com.kingsoft.mcp.bean.BatchInsertCheckInLogRequest;
import com.kingsoft.mcp.bean.CheckInLogDTO;
import com.kingsoft.mcp.bean.InsertCheckInLogRequest;
import com.kingsoft.mcp.bean.QueryCheckInLogRequest;
import com.kingsoft.pojo.CheckInLog;
import com.kingsoft.service.CheckInLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author sunjiacheng
 * @Date 2025/10/21 23:11
 * @PackageName:com.kingsoft.mcp.tool
 * @ClassName: CheckInLogTool
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j
@Component
public class CheckInLogTool {

    @Autowired
    private CheckInLogMapper checkInLogMapper; // 假设已定义对应的Mapper接口

    @Autowired
    private CheckInLogService checkInLogService; // 注入 Service

    /**
     * 插入打卡记录数据
     * @param request 打卡记录参数（由AI解析自然语言后生成）
     * @return 插入结果
     */
    @Tool(description = "向打卡记录日志表（check_in_log）插入数据，需包含员工ID、员工账号、打卡时间、打卡地点、打卡日期")
    public String insertCheckInLog(InsertCheckInLogRequest request) {
        log.info("========调用MCP工具：insertCheckInLog() ===========");
        log.info(String.format("插入打卡记录的参数为：%s", request.toString()));

        try {
            // 1. 转换请求参数为实体类（假设已定义CheckInLog实体类，与表结构对应）
            CheckInLog checkInLog = new CheckInLog();
            // ID自增，无需设置；其他字段映射
            checkInLog.setEmployeeId(request.getEmployeeId());
            checkInLog.setUserId(request.getUserId());
            checkInLog.setCheckTime(request.getCheckTime());
            checkInLog.setLocation(request.getLocation());
            checkInLog.setCheckDate(request.getCheckDate());

            // 2. 执行插入
            int insert = checkInLogMapper.insert(checkInLog);
            if (insert > 0) {
                return String.format("打卡记录插入成功，记录ID：%s", checkInLog.getId());
            } else {
                return "打卡记录插入失败";
            }
        } catch (Exception e) {
            log.error("插入打卡记录异常", e);
            return String.format("插入失败：%s", e.getMessage());
        }
    }

    /**
     * 批量插入打卡记录
     * @param request 批量打卡记录参数（由AI解析自然语言后生成）
     * @return 插入结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Tool(description = "向打卡记录日志表（check_in_log）批量插入数据，参数为多条记录的列表，每条记录需包含员工ID、员工账号、打卡时间、打卡地点、打卡日期")
    public String batchInsertCheckInLog(BatchInsertCheckInLogRequest request) {
        log.info("========调用MCP工具：batchInsertCheckInLog() ===========");
        log.info(String.format("批量插入打卡记录的数量：%d", request.getRecords().size()));

        try {
            // 1. 转换请求参数为实体类列表
            List<CheckInLog> checkInLogs = request.getRecords().stream()
                    .map(record -> {
                        CheckInLog log = new CheckInLog();
                        log.setEmployeeId(record.getEmployeeId());
                        log.setUserId(record.getUserId());
                        log.setCheckTime(record.getCheckTime());
                        log.setLocation(record.getLocation());
                        log.setCheckDate(record.getCheckDate());
                        return log;
                    })
                    .collect(Collectors.toList());

            // 2. 执行批量插入（MyBatis-Plus高效批量插入方法）
            // 注意：需确保CheckInLogMapper继承BaseMapper，且数据库连接URL添加rewriteBatchedStatements=true（MySQL优化）
            boolean batch = checkInLogService.saveBatch(checkInLogs);
            if (batch){
                return String.format("批量插入成功，共插入%d条记录", checkInLogs.size());
            }
            return "批量插入失败";
        } catch (Exception e) {
            log.error("批量插入打卡记录异常", e);
            return String.format("批量插入失败：%s", e.getMessage());
        }
    }


    /**
     * 根据条件查询打卡记录（支持时间范围、员工ID、地点等筛选）
     * @param request 查询条件（由AI解析自然语言生成，包含时间、员工ID等）
     * @return 查询结果（已格式化为Markdown表格字符串，可直接输出）
     */
    @Tool(description = "查询打卡记录日志表（check_in_log）的数据，支持按员工ID、打卡日期（checkDate，格式yyyy-MM-dd）、打卡地点筛选。返回格式化的Markdown表格，可直接展示给用户。")
    public String queryCheckInLog(QueryCheckInLogRequest request) {
        log.info("========调用MCP工具：queryCheckInLog() ===========");
        log.info(String.format("查询参数：%s", request.toString()));

        try {
            QueryWrapper<CheckInLog> queryWrapper = new QueryWrapper<>();

            // 1. 打卡日期精确匹配
            if (request.getCheckDate() != null) {
                queryWrapper.eq("check_date", request.getCheckDate());
            }

            // 2. 员工ID筛选
            if (request.getEmployeeId() != null && !request.getEmployeeId().isEmpty()) {
                queryWrapper.eq("employee_id", request.getEmployeeId());
            }

            // 3. 打卡地点筛选
            if (request.getLocation() != null && !request.getLocation().isEmpty()) {
                queryWrapper.eq("location", request.getLocation());
            }

            // 按打卡时间升序
            queryWrapper.orderByAsc("check_time");

            // 执行查询
            List<CheckInLog> logs = checkInLogMapper.selectList(queryWrapper);

            // 格式化结果 - 直接生成Markdown表格字符串
            if (logs.isEmpty()) {
                return "未查询到符合条件的打卡记录。";
            }
            
            SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
            
            // 构建Markdown表格
            StringBuilder table = new StringBuilder();
            table.append("查询到 ").append(logs.size()).append(" 条打卡记录：\n\n");
            table.append("| 记录ID | 员工ID | 账号 | 打卡时间 | 打卡日期 | 打卡地点 |\n");
            table.append("|--------|--------|------|----------|----------|----------|\n");
            
            for (CheckInLog log : logs) {
                table.append("| ")
                     .append(log.getId() != null ? log.getId() : "")
                     .append(" | ")
                     .append(log.getEmployeeId() != null ? log.getEmployeeId() : "")
                     .append(" | ")
                     .append(log.getUserId() != null ? log.getUserId() : "")
                     .append(" | ")
                     .append(log.getCheckTime() != null ? timeSdf.format(log.getCheckTime()) : "")
                     .append(" | ")
                     .append(log.getCheckDate() != null ? dateSdf.format(log.getCheckDate()) : "")
                     .append(" | ")
                     .append(log.getLocation() != null ? log.getLocation() : "")
                     .append(" |\n");
            }
            
            String result = table.toString();
            log.info("返回格式化表格，长度: {}", result.length());
            return result;
        } catch (Exception e) {
            log.error("查询打卡记录异常", e);
            return "查询打卡记录时发生错误：" + e.getMessage();
        }
    }
}