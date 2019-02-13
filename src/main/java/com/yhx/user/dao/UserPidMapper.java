package com.yhx.user.dao;

import com.yhx.user.model.UserPid;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserPidMapper extends Mapper<UserPid> {

    void batchSavePid(List<UserPid> userPids);

}