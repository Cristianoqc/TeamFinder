package com.yupi.usercenterbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.usercenterbackend.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Prospect
* @description 针对表【user】的数据库操作Service
* @createDate 2024-02-16 15:16:31
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount   账户
     * @param userPassword  密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录
     * @param userAccount 账户
     * @param userPassword 密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签搜索用户
     * @param tagNameList
     * @return
     */
    List<User> searchUsersByTags(List<String> tagNameList);
}
