package com.kingsoft.mcp.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * @Author sunjiacheng
 * @Date 2025/10/20 22:54
 * @PackageName:com.kingsoft.mcp.bean
 * @ClassName: Email
 * @Description: TODO
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    @ToolParam(description = "邮箱地址")
    private String email;
    @ToolParam(description = "邮件标题/主题")
    private String subject;
    @ToolParam(description = "邮件消息/正文内容")
    private String message;
    @ToolParam(description = "邮件内容类型,markdown格式则为1；html格式则为2")
    private Integer contentType;
}
