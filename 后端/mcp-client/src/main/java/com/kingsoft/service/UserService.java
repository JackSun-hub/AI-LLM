package com.kingsoft.service;

import com.kingsoft.bean.LoginRequest;
import com.kingsoft.pojo.User;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户登录
     */
    User login(LoginRequest loginRequest);
    
    /**
     * 用户注册
     */
    User register(User user);
    
    /**
     * 根据ID获取用户
     */
    User getUserById(String userId);
    
    /**
     * 根据用户名获取用户
     */
    User getUserByUsername(String username);
}
