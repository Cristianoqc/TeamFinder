package com.yupi.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.usercenterbackend.common.ErrorCode;
import com.yupi.usercenterbackend.exception.BusinessException;
import com.yupi.usercenterbackend.mapper.TeamMapper;
import com.yupi.usercenterbackend.model.domain.Team;
import com.yupi.usercenterbackend.model.domain.User;
import com.yupi.usercenterbackend.model.domain.UserTeam;
import com.yupi.usercenterbackend.model.dto.TeamQuery;
import com.yupi.usercenterbackend.model.enum_.TeamStatusEnum;
import com.yupi.usercenterbackend.model.request.TeamJoinRequest;
import com.yupi.usercenterbackend.model.request.TeamQuitRequest;
import com.yupi.usercenterbackend.model.request.TeamUpdateRequest;
import com.yupi.usercenterbackend.model.vo.TeamUserVO;
import com.yupi.usercenterbackend.model.vo.UserVO;
import com.yupi.usercenterbackend.service.TeamService;
import com.yupi.usercenterbackend.service.UserService;
import com.yupi.usercenterbackend.service.UserTeamService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
* @author Prospect
*
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
        // 1.请求参数是否为空
        if (team == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.是否登录
        if (loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
        // 3.校验信息
        // 1.队伍人数 1-20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数错误");
        }
        // 2.队伍名称长度 <= 20
        String name = team.getName();
        if (name.length() > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍名称错误");
        }
        // 3.队伍描述长度 <= 512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 512){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述错误");
        }
        // 4.status是否公开(int),不传默认为0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getByValue(status);
        if (statusEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态错误");
        }
        // 5.如果status为2，则密语不能为空，且长度<=32
        if (statusEnum.equals(TeamStatusEnum.SECRET)){
            String password = team.getPassword();
            if (StringUtils.isBlank(password) || password.length() > 32){
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码错误");
            }
        }
        // 6. 超时时间 > 当前时间
        Date expeiretime = team.getExpireTime();
        if (new Date().after(expeiretime)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间错误");
        }
        // 7. 校验用户最多创建5个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long hasTeamNum = this.count(queryWrapper);
        if (hasTeamNum >= 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最多只能创建5个队伍");
        }
        //  8.插入队伍信息到队伍列表
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result || teamId == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败");
        }
        // 9. 插入用户-队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败");
        }
        return teamId;
    }

    @Override
    public List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        // 1. 查询条件
        if (teamQuery != null){
            Long id = teamQuery.getId();
            if (id != null && id > 0){
                queryWrapper.eq("id", id);
            }
            List<Long> idList = teamQuery.getIdList();
            if (CollectionUtils.isNotEmpty(idList)){
                queryWrapper.in("id", idList);
            }
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)){
                queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
            }
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)){
                queryWrapper.like("name", name);
            }
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)){
                queryWrapper.like("description", description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0){
                queryWrapper.eq("maxNum", maxNum);
            }
            Long userId = teamQuery.getUserId();
            if (userId != null && userId > 0){
                queryWrapper.eq("userId", userId);
            }
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getByValue(status);
            if (statusEnum == null){
                statusEnum = TeamStatusEnum.PUBLIC;
            }
            if (!isAdmin && statusEnum.equals(TeamStatusEnum.PRIVATE)){
                throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
            }
            queryWrapper.eq("status", statusEnum.getValue());
        }
        // 不显示已过期的队伍
        queryWrapper.and(qw -> qw.gt("expireTime", new Date()).or().isNull("expireTime"));
        List<Team> teamList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)){
            return new ArrayList<>();
        }
        ArrayList<TeamUserVO> teamUserVOList = new ArrayList<>();
        // 关联查询创建人用户信息
        for (Team team : teamList) {
            Long userId = team.getUserId();
            if (userId == null || userId <= 0){
                continue;
            }
            User user = userService.getById(userId);
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team, teamUserVO);
            // 脱敏
            if (user != null){
                UserVO createUser = new UserVO();
                BeanUtils.copyProperties(user, createUser);
                teamUserVO.setCreateUser(createUser);
            }
            teamUserVOList.add(teamUserVO);
        }
        return teamUserVOList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Long teamId = teamUpdateRequest.getId();
        Team oldTeam = this.getTeamById(teamId);
        if (oldTeam.getUserId() != loginUser.getId() && !userService.isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 修改队伍为加密状态时必须要传入密码
        TeamStatusEnum statusEnum = TeamStatusEnum.getByValue(teamUpdateRequest.getStatus());
        if (statusEnum.equals(TeamStatusEnum.SECRET) && StringUtils.isBlank(teamUpdateRequest.getPassword())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能为空");
        }
        Team updateTeam = new Team();
        BeanUtils.copyProperties(teamUpdateRequest, updateTeam);
        return this.updateById(updateTeam);
    }

    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        if (teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Long teamId = teamJoinRequest.getTeamId();
        Team team = this.getTeamById(teamId);
        Date expireTime = team.getExpireTime();
        if (expireTime != null && expireTime.before(new Date())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getByValue(status);
        if (TeamStatusEnum.PRIVATE.equals(statusEnum)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "禁止加入私有队伍");
        }
        if (TeamStatusEnum.SECRET.equals(statusEnum)){
            String password = teamJoinRequest.getPassword();
            if (StringUtils.isBlank(password)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能为空");
            }
            if (!team.getPassword().equals(password)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
            }
        }
        // 加入队伍的数量
        Long userId = loginUser.getId();
        // 分布式锁
        RLock rLock = redissonClient.getLock("yupao:join_team");
        try {
            // 抢锁并执行
            while (true){
                if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)){
                    QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
                    userTeamQueryWrapper.eq("userId", userId);
                    long hasJoinNum = userTeamService.count(userTeamQueryWrapper);
                    if (hasJoinNum >= 5){
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "已加入队伍上限");
                    }
                    // 不能重复加入已加入的队伍
                    userTeamQueryWrapper = new QueryWrapper<>();
                    userTeamQueryWrapper.eq("userId", userId);
                    userTeamQueryWrapper.eq("teamId", teamId);
                    long hasUserJoinTeam = userTeamService.count(userTeamQueryWrapper);
                    if (hasUserJoinTeam > 0){
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能重复加入已加入的队伍");
                    }
                    // 已加入队伍的人数
                    long teamHasJoinNum = this.countTeamUserByTeamId(teamId);
                    if (teamHasJoinNum >= team.getMaxNum()){
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数已满");
                    }
                    // 新增用户-队伍关联信息
                    UserTeam userTeam = new UserTeam();
                    userTeam.setUserId(userId);
                    userTeam.setTeamId(teamId);
                    userTeam.setJoinTime(new Date());
                    return userTeamService.save(userTeam);
                }
            }
        } catch (InterruptedException e) {
            log.error("抢锁失败", e);
            return false;
        } finally {
            // 只能释放自己的锁
            if (rLock.isHeldByCurrentThread()){
                rLock.unlock();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {
        if (teamQuitRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Long teamId = teamQuitRequest.getTeamId();
        Team team = this.getTeamById(teamId);
        long userId = loginUser.getId();
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>(userTeam);
        long count = userTeamService.count(queryWrapper);
        if (count == 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有加入该队伍");
        }
        long teamHasJoinNum = this.countTeamUserByTeamId(teamId);
        // 队伍人数为1，删除队伍
        if (teamHasJoinNum == 1){
            // 删除队伍
            this.removeById(teamId);
        } else {
            // 是否为队长
            if (team.getUserId().equals(userId)){
                // 把队伍转移给最早加入的用户
                QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
                userTeamQueryWrapper.eq("teamId", teamId);
                userTeamQueryWrapper.last("order by id asc limit 2");
                List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
                if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() <= 1){
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "没有找到最早加入的用户");
                }
                UserTeam nextUserTeam = userTeamList.get(1);
                Long nextTeamLeaderId = nextUserTeam.getUserId();
                // 更新当前队伍的队长
                Team updateTeam = new Team();
                updateTeam.setId(teamId);
                updateTeam.setUserId(nextTeamLeaderId);
                boolean result = this.updateById(updateTeam);
                if (!result){
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队伍队长失败");
                }
            }
        }
        // 删除用户-队伍关系信息
        return userTeamService.remove(queryWrapper);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeam(long id, User loginUser) {
        Team team = getTeamById(id);
        if (!team.getUserId().equals(loginUser.getId())){
            throw new BusinessException(ErrorCode.NO_AUTH, "只有队长才能解散队伍");
        }
        // 移除所有加入队伍的关联信息
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId", id);
        boolean result = userTeamService.remove(userTeamQueryWrapper);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "解散队伍失败");
        }
        // 删除队伍
        return this.removeById(id);

    }


    /**
     * 校验队伍是否存在
     * @param teamId
     * @return
     */
    private Team getTeamById(Long teamId) {
        if (teamId == null || teamId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null){
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        return team;
    }

    /**
     * 获取队伍人数
     * @param teamId
     * @return
     */
    private long countTeamUserByTeamId(long teamId) {
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", teamId);
        return userTeamService.count(queryWrapper);
    }
}




