package com.kingsoft.service.impl;

import com.kingsoft.bean.LoginRequest;
import com.kingsoft.mapper.UserMapper;
import com.kingsoft.pojo.User;
import com.kingsoft.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 用户服务数据库实现类
 * 使用@Primary注解，优先使用此实现（需要先配置数据库）
 */
@Slf4j
@Service
@Primary  // 优先使用此实现，如果数据库未配置，请注释此行
public class UserServiceDbImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public User login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        
        log.info("用户登录（数据库）: username={}", username);
        
        try {
            User user = userMapper.findByUsername(username);
            
            if (user == null) {
                log.warn("用户不存在: {}", username);
                return null;
            }
            
            // 验证密码（实际项目应该使用加密后的密码比对）
            if (!password.equals(user.getPassword())) {
                log.warn("密码错误: {}", username);
                return null;
            }
            
            // 检查用户状态
            if (user.getStatus() != 0) {
                log.warn("用户已被禁用: {}", username);
                return null;
            }
            
            log.info("登录成功: {}", username);
            return user;
        } catch (Exception e) {
            log.error("登录失败", e);
            return null;
        }
    }
    
    @Override
    public User register(User user) {
        String username = user.getUsername();
        
        log.info("用户注册（数据库）: username={}", username);
        
        try {
            // 检查用户名是否已存在
            if (userMapper.countByUsername(username) > 0) {
                log.warn("用户名已存在: {}", username);
                return null;
            }
            
            // 生成用户ID
            String userId = "user_" + UUID.randomUUID().toString().replace("-", "");
            user.setId(userId);
            user.setIsGuest(false);
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            user.setStatus(0);
            
            // 设置默认头像
            if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
                user.setAvatar("https://ui-avatars.com/api/?name=" + username + "&background=667eea&color=fff");
            }
            
            // 保存到数据库
            userMapper.insert(user);
            
            log.info("注册成功: userId={}, username={}", userId, username);
            return user;
        } catch (Exception e) {
            log.error("注册失败", e);
            return null;
        }
    }
    
    @Override
    public User getUserById(String userId) {
        try {
            return userMapper.findById(userId);
        } catch (Exception e) {
            log.error("查询用户失败: userId={}", userId, e);
            return null;
        }
    }
    
    @Override
    public User getUserByUsername(String username) {
        try {
            return userMapper.findByUsername(username);
        } catch (Exception e) {
            log.error("查询用户失败: username={}", username, e);
            return null;
        }
    }
}
