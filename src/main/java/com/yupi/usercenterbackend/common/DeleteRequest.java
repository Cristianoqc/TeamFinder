package com.yupi.usercenterbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用删除请求
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = 8253008328104294904L;

    private long id;

}
