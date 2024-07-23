package com.yupi.usercenterbackend.service;

import com.yupi.usercenterbackend.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.usercenterbackend.model.domain.User;
import com.yupi.usercenterbackend.model.dto.TeamQuery;
import com.yupi.usercenterbackend.model.request.TeamJoinRequest;
import com.yupi.usercenterbackend.model.request.TeamQuitRequest;
import com.yupi.usercenterbackend.model.request.TeamUpdateRequest;
import com.yupi.usercenterbackend.model.vo.TeamUserVO;

import java.util.List;

/**
* @author Prospect
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     */
    long addTeam(Team team, User loginUser);

    /**
     * 查找队伍
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);


    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除队伍
     * @param id
     * @return
     */
    boolean deleteTeam(long id, User loginUser);
}
