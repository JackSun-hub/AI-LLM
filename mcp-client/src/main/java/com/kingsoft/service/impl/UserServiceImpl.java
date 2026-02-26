package com.kingsoft.service.impl;

import com.kingsoft.bean.LoginRequest;
import com.kingsoft.pojo.User;
import com.kingsoft.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 用户服务实现类（内存存储版本，实际项目应使用数据库）
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    
    // 使用内存存储用户信息（实际项目应使用数据库）
    private static final Map<String, User> userMap = new ConcurrentHashMap<>();
    private static final Map<String, User> usernameMap = new ConcurrentHashMap<>();
    
    // 初始化一个测试用户
    static {
        User testUser = new User();
        testUser.setId("user_test_001");
        testUser.setUsername("admin");
        testUser.setPassword("123456"); // 实际项目应该加密
        testUser.setEmail("admin@example.com");
        testUser.setAvatar("https://ui-avatars.com/api/?name=Admin&background=667eea&color=fff");
        testUser.setIsGuest(false);
        testUser.setCreateTime(LocalDateTime.now());
        testUser.setStatus(0);
        
        userMap.put(testUser.getId(), testUser);
        usernameMap.put(testUser.getUsername(), testUser);
        
        log.info("初始化测试用户: username=admin, password=123456");
    }
    
    @Override
    public User login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        
        log.info("用户登录: username={}", username);
        
        // 从内存中查找用户
        User user = usernameMap.get(username);
        
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
    }
    
    @Override
    public User register(User user) {
        String username = user.getUsername();
        
        log.info("用户注册: username={}", username);
        
        // 检查用户名是否已存在
        if (usernameMap.containsKey(username)) {
            log.warn("用户名已存在: {}", username);
            return null;
        }
        
        // 生成用户ID
        String userId = "user_" + UUID.randomUUID().toString().replace("-", "");
        user.setId(userId);
        user.setIsGuest(false);
        user.setCreateTime(LocalDateTime.now());
        user.setStatus(0);
        
        // 设置默认头像
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            user.setAvatar("https://ui-avatars.com/api/?name=" + username + "&background=667eea&color=fff");
        }
        
        // 保存到内存
        userMap.put(userId, user);
        usernameMap.put(username, user);
        
        log.info("注册成功: userId={}, username={}", userId, username);
        return user;
    }
    
    @Override
    public User getUserById(String userId) {
        return userMap.get(userId);
    }
    
    @Override
    public User getUserByUsername(String username) {
        return usernameMap.get(username);
    }
}
