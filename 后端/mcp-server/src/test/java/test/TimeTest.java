package test;

import com.kingsoft.ApplicationService;
import com.kingsoft.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 15:01
 * @PackageName:com.test
 * @ClassName: TimeTest
 * @Description: TODO
 * @Version 1.0
 */
@SpringBootTest(classes = ApplicationService.class)
public class TimeTest {
    @Autowired
    private ProductMapper productMapper;

    @Test
    public void testDelete() {
        Long testId = 1L; // 替换为实际存在的主键
        int rows = productMapper.deleteById("1234");
        System.out.println("删除行数：" + rows); // 若正常输出，说明基础配置没问题
    }
}
