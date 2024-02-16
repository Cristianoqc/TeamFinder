package com.yupi.usercenterbackend.service;

import com.yupi.usercenterbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Prospect
* @description 针对表【user】的数据库操作Service
* @createDate 2024-02-16 15:16:31
*/
public interface UserService extends IService<User> {

    /**
     *
     * @param userAccount   账户
     * @param userPassword  密码
     * @param checkPassword 校验密码
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);
}
