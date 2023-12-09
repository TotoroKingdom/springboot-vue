package com.totoro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SpringbootVueBackendApplicationTests {

    @Test
    void contextLoads() {

        String encode = new BCryptPasswordEncoder().encode("123456");
        System.out.println(encode);

    }

}
