//package com.yupi.usercenterbackend.service;
//
//import com.yupi.usercenterbackend.model.domain.User;
//import org.junit.Assert;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
///**
// * 用户服务测试
// */
//@SpringBootTest
//class UserServiceTest {
//
//    @Resource
//    private UserService userService;
//
//    @Test
//    void testAddUser(){
//        User user = new User();
//        user.setUsername("dogyupi");
//        user.setUserAccount("123");
//        user.setAvatarUrl("123456");
//        user.setGender(0);
//        user.setUserPassword("123456");
//        user.setPhone("123456");
//        user.setEmail("123456");
//        boolean result = userService.save(user);
//        System.out.println(user.getId());
//        assertTrue(result);
//    }
//
//    @Test
//    void userRegister() {
//        String userAccount = "sserssr";
//        String userPassword = "12345678";
//        String checkPassword = "12345678";
//        String planetCode = "3795";
//        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertTrue(result > 0);
//    }
//
//    @Test
//    void searchUsersByTags() {
//        List<String> tagNameList = Arrays.asList("java", "python");
//        List<User> userList = userService.searchUsersByTags(tagNameList);
//        assertNotNull(userList);
//    }
//}