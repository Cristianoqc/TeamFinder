package com.yupi.usercenterbackend.model.dto;

import com.yupi.usercenterbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * 队伍查询封装类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageRequest {
    /**
     *
     */
    private Long id;

    /**
     * 队伍Id列表
     */
    private List<Long> idList;

    /**
     * 搜索关键词(同时对名称和描述进行搜索)
     */
    private String searchText;

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
     * 用户Id
     */
    private Long userId;

    /**
     * 0-公开，1-私有，2-加密
     */
    private Integer status;
}
