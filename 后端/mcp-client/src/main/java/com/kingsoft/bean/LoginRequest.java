package com.kingsoft.bean;

import lombok.Data;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 登录请求类
 */
@Data
public class LoginRequest {
    
    private String username;
    
    private String password;
    
    private Boolean rememberMe;
}
