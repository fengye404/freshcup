package sast.freshcup;

import cn.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SastFreshCupApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(SecureUtil.md5("1"));
    }

}
