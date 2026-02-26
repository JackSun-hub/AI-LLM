package com.kingsoft.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingsoft.mapper.CheckInLogMapper;
import com.kingsoft.pojo.CheckInLog;
import com.kingsoft.service.CheckInLogService;
import org.springframework.stereotype.Service;
/**
 * @Author sunjiacheng
 * @Date 2025/10/21 23:21
 * @PackageName:com.kingsoft.service.Impl
 * @ClassName: CheckInLogServiceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
public class CheckInLogServiceImpl extends ServiceImpl<CheckInLogMapper, CheckInLog> implements CheckInLogService {
}
