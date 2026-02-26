package com.test;

import com.kingsoft.ApplicationClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 15:01
 * @PackageName:com.test
 * @ClassName: TimeTest
 * @Description: TODO
 * @Version 1.0
 */
@SpringBootTest(classes = ApplicationClient.class)
public class TimeTest {
    @Test
    public void testTime() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("任务1");
        Thread.sleep(1000);
        stopWatch.stop();

        stopWatch.start("任务2");
        Thread.sleep(300);
        stopWatch.stop();

        stopWatch.start("任务3");
        Thread.sleep(100);
        stopWatch.stop();
        //打印任务耗时
        System.out.println(stopWatch.prettyPrint());
        System.out.println(stopWatch.shortSummary());
        //打印任务总数
        System.out.println("所有任务总耗时：" + stopWatch.getTotalTimeMillis());
        System.out.println("任务总数：" + stopWatch.getTaskCount());
    }

    void test(String[][] arr){
        int a = 0;
       int m =arr.length;
        int n=arr[0].length;
        for(int i =0 ; i<m ;i++){
            for (int j=0;j<n;j++){
                //战舰是X
                if(arr[i][j].equals("X")){
                    if (arr[i][j].equals("X")) {
                        if (0==i || "X".equals(arr[i-1][j]) && (j==0 || "X".equals(arr[i][j-1]))){
                            a++;
                        }
                    }
                }
            }

        }
        System.out.println(a);
        }
}
