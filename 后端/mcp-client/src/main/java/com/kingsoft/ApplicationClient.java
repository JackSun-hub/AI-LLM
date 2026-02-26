package com.kingsoft;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 11:42
 * @PackageName:com.kingsoft
 * @ClassName: Application
 * @Description: TODO
 * @Version 1.0
 */
@SpringBootApplication
public class ApplicationClient {
    public static void main(String[] args) {
        //加载.env文件
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        //把.env文件里的值设置到环境变量中
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(ApplicationClient.class, args);
    }
}
