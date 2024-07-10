package com.yupi.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.usercenterbackend.model.domain.Team;
import com.yupi.usercenterbackend.service.TeamService;
import com.yupi.usercenterbackend.mapper.TeamMapper;
import org.springframework.stereotype.Service;

/**
* @author Prospect
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-07-10 13:30:17
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

}




