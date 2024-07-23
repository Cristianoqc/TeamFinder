package com.yupi.usercenterbackend.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = -356252907665983695L;
    /**
     * 团队id
     */
    private Long teamId;
}
