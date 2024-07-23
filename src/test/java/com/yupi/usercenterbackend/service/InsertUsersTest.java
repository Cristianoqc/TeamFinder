//package com.yupi.usercenterbackend.service;
//
//import com.yupi.usercenterbackend.model.domain.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.StopWatch;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.*;
//
//@SpringBootTest
//public class InsertUsersTest {
//    @Resource
//    private UserService userService;
//
//    // 自定义线程池
//    private ExecutorService executorService = new ThreadPoolExecutor(60, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));
//
//    /**
//     * 插入用户
//     */
//    @Test
//    public void doInsertUsers(){
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        final int INSERT_NUM = 100000;
//        List<User> userList = new ArrayList<>();
//        for (int i = 0; i < INSERT_NUM; i++) {
//            User user = new User();
//            user.setUsername("假用户");
//            user.setUserAccount("fake");
//            user.setAvatarUrl("");
//            user.setGender(0);
//            user.setUserPassword("12345678");
//            user.setPhone("123");
//            user.setEmail("123");
//            user.setTags("[]");
//            user.setUserStatus(0);
//            user.setUserRole(0);
//            user.setPlanetCode("1111111111");
//            userList.add(user);
//        }
//        userService.saveBatch(userList, 10000);   // 批量插入，提高插入效率
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
//    }
//
//
//    /**
//     * 并发批量插入用户
//     */
//    @Test
//    public void doConcurrentInsertUsers() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        final int INSERT_NUM = 100000;
//        int batchSize = 10000;
//        // 分组
//        int j = 0;
//        List<CompletableFuture<Void>> futureList = new ArrayList<>();
//        for (int i = 0; i < 40; i++) {
//            List<User> userList = new ArrayList<>();
//            while(true){
//                j++;
//                User user = new User();
//                user.setUsername("假用户");
//                user.setUserAccount("fake");
//                user.setAvatarUrl("");
//                user.setGender(0);
//                user.setUserPassword("12345678");
//                user.setPhone("123");
//                user.setEmail("123");
//                user.setTags("[]");
//                user.setUserStatus(0);
//                user.setUserRole(0);
//                user.setPlanetCode("1111111111");
//                userList.add(user);
//                if (j % batchSize == 0){
//                    break;
//                }
//            }
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                userService.saveBatch(userList, batchSize);
//            }, executorService);
//            futureList.add(future);
//        }
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
//
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
//    }
//}
//
