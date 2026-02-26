package com.kingsoft.enums;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 17:20
 * @PackageName:com.kingsoft.enums
 * @ClassName: SSEMsgType
 * @Description: 发送SSE消息的类型枚举
 * @Version 1.0
 */
public enum SSEMsgType {
    MESSAGE("message","单次发送的普通类型消息"),
    ADD("add","消息追加，适用于流式Stream的消息推送"),
    FINISH("finish","消息完成"),
    CUSTOM_EVENT("custom_event","自定义的消息类型"),
    DONE("done","消息完成"); //chatGLM v4

    public final String type;
    public final String value;
    SSEMsgType(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
