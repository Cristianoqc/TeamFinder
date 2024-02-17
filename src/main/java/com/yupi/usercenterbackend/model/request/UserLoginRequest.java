package com.yupi.usercenterbackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -7955967681383159333L;

    private String userAccount;

    private String userPassword;
}
