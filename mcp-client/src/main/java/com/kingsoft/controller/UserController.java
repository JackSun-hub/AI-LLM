package com.kingsoft.controller;

import com.kingsoft.bean.LoginRequest;
import com.kingsoft.pojo.User;
import com.kingsoft.service.UserService;
import com.kingsoft.utils.SunResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Author sunjiacheng
 * @Date 2025/12/25
 * @Description: 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public SunResult login(@RequestBody LoginRequest loginRequest) {
        log.info("用户登录请求: username={}", loginRequest.getUsername());
        
        try {
            User user = userService.login(loginRequest);
            if (user != null) {
                // 不返回密码
                user.setPassword(null);
                return SunResult.ok(user);
            } else {
                return SunResult.errorMsg("用户名或密码错误");
            }
        } catch (Exception e) {
            log.error("登录失败", e);
            return SunResult.errorMsg("登录失败: " + e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public SunResult register(@RequestBody User user) {
        log.info("用户注册请求: username={}", user.getUsername());
        
        try {
            User newUser = userService.register(user);
            if (newUser != null) {
                newUser.setPassword(null);
                return SunResult.ok(newUser);
            } else {
                return SunResult.errorMsg("注册失败，用户名已存在");
            }
        } catch (Exception e) {
            log.error("注册失败", e);
            return SunResult.errorMsg("注册失败: " + e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public SunResult logout() {
        log.info("用户登出");
        // 这里可以清除session或token
        return SunResult.ok();
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public SunResult getUserInfo(@RequestParam(required = false) String userId) {
        log.info("获取用户信息: userId={}", userId);
        
        try {
            if (userId == null || userId.isEmpty()) {
                return SunResult.errorMsg("用户ID不能为空");
            }
            
            User user = userService.getUserById(userId);
            if (user != null) {
                user.setPassword(null);
                return SunResult.ok(user);
            } else {
                return SunResult.errorMsg("用户不存在");
            }
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return SunResult.errorMsg("获取用户信息失败: " + e.getMessage());
        }
    }
}
