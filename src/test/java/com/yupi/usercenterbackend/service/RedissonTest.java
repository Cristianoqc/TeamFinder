package com.yupi.usercenterbackend.service;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;



@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void test(){
        // list
        RList<String> rList = redissonClient.getList("test-list");
        rList.add("1");
        rList.get(0);
//        rList.remove(0);

        // map

        // set

        // stack
    }
}
