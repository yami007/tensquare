import com.tensquare.base.BaseApplication;
import com.tensquare.base.redisson.utils.RedissLockUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaseApplication.class)
public class RdissonTest {
    @Test
    public void rdissonTest() {
        for (int i = 1; i <= 100; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String key ="123";
                    RedissLockUtil.tryLock(key, TimeUnit.SECONDS,4,3);
                    System.out.println(Thread.currentThread().getName()+"线程获取到了锁====开始执行操作");
                    RedissLockUtil.unlock(key);
                    System.out.println(Thread.currentThread().getName()+"线程释放了锁======");

                }
            });
            thread.start();
        }
    }
}
