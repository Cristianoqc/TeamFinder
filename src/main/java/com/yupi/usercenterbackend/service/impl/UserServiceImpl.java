package com.yupi.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.usercenterbackend.model.domain.User;
import com.yupi.usercenterbackend.service.UserService;
import com.yupi.usercenterbackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yupi.usercenterbackend.model.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author Prospect
* 用户服务实现类
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 密码盐值
     */
    private static final String SALT = "yupi";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return -1;
        }
        //校验账户
        if (userAccount.length() < 4){
            return -1;
        }
        //校验密码
        if (userPassword.length() < 8 || checkPassword.length() < 8){
            return -1;
        }
        //账户不能包含特殊字符
        String validPattern = "^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()){
            return -1;
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            return -1;
        }
        //密码何校验码相同
        if (!userPassword.equals(checkPassword)){
            return -1;
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult){
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        //校验账户
        if (userAccount.length() < 4){
            return null;
        }
        //校验密码
        if (userPassword.length() < 8){
            return null;
        }
        //账户不能包含特殊字符
        String validPattern = "^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()){
            return null;
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null){
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }

        //3.用户脱敏
        User safeyUser = getSafetyUser(user);

        //4.记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safeyUser);
        return safeyUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        User safeyUser = new User();
        safeyUser.setId(originUser.getId());
        safeyUser.setUsername(originUser.getUsername());
        safeyUser.setUserAccount(originUser.getUserAccount());
        safeyUser.setAvatarUrl(originUser.getAvatarUrl());
        safeyUser.setGender(originUser.getGender());
        safeyUser.setPhone(originUser.getPhone());
        safeyUser.setEmail(originUser.getEmail());
        safeyUser.setUserRole(originUser.getUserRole());
        safeyUser.setUserStatus(originUser.getUserStatus());
        safeyUser.setCreateTime(originUser.getCreateTime());
        return safeyUser;
    }
}




