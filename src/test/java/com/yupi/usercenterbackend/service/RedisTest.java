package com.yupi.usercenterbackend.service;
import java.util.Date;

import com.yupi.usercenterbackend.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 增
        valueOperations.set("yupiString", "dog");
        valueOperations.set("yupiInt", 1);
        valueOperations.set("yupiDouble", 2.0);
        User user = new User();
        user.setId(0L);
        user.setUsername("");
        valueOperations.set("yupiUser", user);

        // 查
        Object yupiString = valueOperations.get("yupiString");
        Assertions.assertTrue("dog".equals((String)yupiString));
        Object yupiInt = valueOperations.get("yupiInt");
        Assertions.assertTrue(1 == (Integer)yupiInt);
        Object yupiDouble = valueOperations.get("yupiDouble");
        Assertions.assertTrue(2.0 == (Double)yupiDouble);
        Object yupiUser = valueOperations.get("yupiUser");
        System.out.println(valueOperations.get("yupiUser"));
    }
}
