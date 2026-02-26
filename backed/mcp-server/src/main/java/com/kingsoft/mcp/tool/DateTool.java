package com.kingsoft.mcp.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author sunjiacheng
 * @Date 2025/10/20 22:23
 * @PackageName:com.kingsoft.mcp.tool
 * @ClassName: DateTool
 * @Description: TODO
 * @Version 1.0
 */
@Component
@Slf4j
public class DateTool {

    @Tool(description = "根据城市所在的市区id来获取当前的时间")
    public String getCurrentTimeByZoneId(String cityName,String zoneId) {
        log.info("========调用MCP工具：getCurrentTimeByZoneId() ===========");
        ZoneId zone = ZoneId.of(zoneId);
        //获取该时区当前的时间
        ZonedDateTime now = ZonedDateTime.now(zone);
        // 直接返回完整的时间字符串，不使用任何格式化
        String timeStr = now.toString();
        String currentTime = "Current time is: " + timeStr;
        log.info("返回时间: {}", currentTime);
        return currentTime;
    }
    @Tool(description = "获取当前时间")
    public String getCurrentTime() {
        log.info("========调用MCP工具：getCurrentTime() ===========");
        String currentTime = String.format("当前的时间是 %s",LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return currentTime;
    }
}
