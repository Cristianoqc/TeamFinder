package com.yupi.usercenterbackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TeamUserVO implements Serializable {

    private static final long serialVersionUID = 3512573608713166716L;
    /**
     *
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
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 0-公开，1-私有，2-加密
     */
    private Integer status;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     * 创建人用户列表
     */
    private UserVO createUser;

    /**
     * 加入用户数
     */
    private Integer hasJoinNum;

    /**
     * 用户是否以加入
     */
    private boolean hasJoin = false;

}
