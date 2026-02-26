package com.kingsoft;
import com.kingsoft.mcp.tool.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 11:42
 * @PackageName:com.kingsoft
 * @ClassName: Application
 * @Description: TODO
 * @Version 1.0
 */
@MapperScan("com.kingsoft.mapper")
@SpringBootApplication
public class ApplicationService {

    //http://localhost:9060/sse
    public static void main(String[] args) {
        SpringApplication.run(ApplicationService.class, args);
    }
    /**
     * 注册MCP工具
     * @param dateTool
     * @return
     */
    @Bean
    public ToolCallbackProvider registMCPTools(DateTool dateTool
            , EmailTool emailTool
            , ProductTool productTool
            , TableCreateTool tableCreateTool
            , CheckInLogTool checkInLogTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(dateTool,emailTool,productTool,tableCreateTool,checkInLogTool)
                .build();
    }
}
