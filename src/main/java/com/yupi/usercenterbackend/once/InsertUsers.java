package com.yupi.usercenterbackend.once;
import java.util.Date;

import com.yupi.usercenterbackend.mapper.UserMapper;
import com.yupi.usercenterbackend.model.domain.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

//@Component
public class InsertUsers {
//    @Resource
    private UserMapper userMapper;

    /**
     * 插入用户
     */
//    @Scheduled(initialDelay = 5000, fixedRate = Long.MAX_VALUE)
    public void doInsertUsers(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假用户");
            user.setUserAccount("fake");
            user.setAvatarUrl("");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("123");
            user.setEmail("123");
            user.setTags("[]");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("1111111111");

            userMapper.insert(user);
        }
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
