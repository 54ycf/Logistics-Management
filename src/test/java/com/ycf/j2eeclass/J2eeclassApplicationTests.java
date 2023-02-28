package com.ycf.j2eeclass;

import com.ycf.j2eeclass.security.Md5;
import com.ycf.j2eeclass.security.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class J2eeclassApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(Md5.EncoderByMd5("222222"));
    }
}
