package com.kingsoft.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 14:38
 * @PackageName:com.kingsoft.aop
 * @ClassName: SevcieLogAspect
 * @Description: TODO
 * @Version 1.0
 */
@Component
@Slf4j
@Aspect
public class SevcieLogAspect {

    /*
     * @Description: AOP 切面，记录方法执行时间
     *                    * 表示返回所有类型，void 表示返回值为void，也可以是其他类型
     *                    com.kingsoft.service.impl 指定的包名，要切的类的所在包
     *                    ..可以匹配当前包和子包下的类
     *                    * 匹配当前包以及子包下的类
     *                    . 无意义
     *                    * 表示匹配任意方法名
     *                     (..) 表示匹配任意方法参数
     * @param joinPoint
     * @return java.lang.Object
     * @Author: sunjiacheng
     * @Date: 2025/10/14 14:43
     */
    @Around("execution(* com.kingsoft.service.impl..*.*(..))")
    public Object recordTimeslog(ProceedingJoinPoint joinPoint) throws Throwable {
        //log.info("进入方法：{}", joinPoint.getSignature().getName());
       // long begin = System.currentTimeMillis();
        //通过使用秒表记录方式耗时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = joinPoint.proceed();
        String point = joinPoint.getTarget().getClass().getName()
                + "."
                + joinPoint.getSignature().getName();

       // long end = System.currentTimeMillis();
        //log.info("退出方法：{}", joinPoint.getSignature().getName());
       // long takeTime = end - begin;
        stopWatch.stop();
        long takeTime =stopWatch.getTotalTimeMillis();
        if (takeTime > 3000){
            log.error("{} 耗时偏长：{}ms", point, takeTime);
        }else if (takeTime > 2000){
            log.warn("{} 耗时中等：{}ms", point, takeTime);
        }else {
            log.info("{} 耗时：{}ms", point, takeTime);
        }
        return proceed;
    }
}
