package com.kingsoft.mcp.tool;

import com.kingsoft.mcp.bean.EmailRequest;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
public class EmailTool {
    @Resource
    private  JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private  String from;

    //以下代码也是注入的方式
//    public EmailTool(JavaMailSender mailSender, @Value("${spring.mail.username}") String from) {
//        this.mailSender = mailSender;
//        this.from = from;
//    }

    @Tool(description = "给指定的邮箱发送邮件信息，email为收件人邮箱，subject为邮件标题，message为邮件内容")
    public void sendMailMessage(EmailRequest emailRequest) {
        log.info("========调用MCP工具：sendMailMessage() ===========");
        Integer contentType = emailRequest.getContentType();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom(from); //这个from 可以直接获取到当前登录人的邮箱地址，以他作为发送人
            mimeMessageHelper.setTo(emailRequest.getEmail());
            mimeMessageHelper.setSubject(emailRequest.getSubject());
            //mimeMessageHelper.setText(emailRequest.getMessage());
            if (contentType == 1){
                mimeMessageHelper.setText(convertToHtml(emailRequest.getMessage()),true);
            } else if (contentType == 2) {
                mimeMessageHelper.setText(emailRequest.getMessage(),true);
            } else {
                mimeMessageHelper.setText(emailRequest.getMessage());
            }
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            //throw new RuntimeException(e);
            log.error("==========发送邮件失败，报错信息：{}===========",e.getMessage());
        }
    }

    @Tool(description = "查询我的邮箱地址/邮箱地址")//也可以改成查询我老板的邮箱
    public String getMyEmailAddress() { //这里也可进行数据库操作，查询数据库
        log.info("========调用MCP工具：getMyEmailAddress() ===========");

        return "sunjiacheng999@163.com";
    }


    /**
     * markdown 转 html
     * @param markDownText
     * @return
     */
    public static String convertToHtml(String markDownText) {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        return renderer.render(parser.parse(markDownText));
    }
}
