package com.yupi.usercenterbackend.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class TeamUpdateRequest {

    /**
     * 队伍ID
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 0-公开，1-私有，2-加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;
}
