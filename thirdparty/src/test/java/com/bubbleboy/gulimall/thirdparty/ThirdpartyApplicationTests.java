package com.bubbleboy.gulimall.thirdparty;


import com.aliyun.oss.OSS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThirdpartyApplicationTests {


    @Autowired
    OSS ossClient;

    @Test
    public void testOSS() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream("C:\\Users\\lishi\\OneDrive\\桌面\\com.oculus.shellenv-20240512-164755.jpg");
        ossClient.putObject("gulimall-bubbleboy","bugg.jpg",inputStream);
    }

}
