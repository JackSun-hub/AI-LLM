package com.kingsoft.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 22:35
 * @PackageName:com.kingsoft.bean
 * @ClassName: ChatEntity
 * @Description: TODO
 * @Version 1.0
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseEntity {
    private String message;
    private String botMsgId;
}
