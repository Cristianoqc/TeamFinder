package com.yupi.usercenterbackend.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = -731566293028625029L;
    /**
     * 团队id
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;
}
