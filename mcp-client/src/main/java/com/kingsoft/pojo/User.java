package com.kingsoft.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 用户实体类
 */
@Data
public class User {
    
    private String id;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private String avatar;
    
    private Boolean isGuest;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Integer status; // 0-正常 1-禁用
}
