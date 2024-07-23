package com.yupi.usercenterbackend.model.vo;

import java.util.Date;

public class UserVO {
    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String username;

    /**
     *
     */
    private String userAccount;

    /**
     *
     */
    private String avatarUrl;

    /**
     *
     */
    private Integer gender;

    /**
     *
     */
    private String phone;

    /**
     *
     */
    private String email;

    /**
     *
     */
    private String tags;

    /**
     *
     */
    private Integer userStatus;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     * 0普通用户 1管理员
     */
    private Integer userRole;

    /**
     * 星球编号
     */
    private String planetCode;
}
