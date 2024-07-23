package com.yupi.usercenterbackend.common;


import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页请求参数
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 2350485058382999457L;


    protected int pageSize = 10;

    protected int pageNum = 1;
}
