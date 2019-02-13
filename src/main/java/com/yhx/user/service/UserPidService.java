package com.yhx.user.service;

import com.yhx.common.service.BaseService;
import com.yhx.user.model.UserPid;

public interface UserPidService extends BaseService<UserPid> {

    public void createUserPid(String namePrefix, int start, int end);

    public boolean syncUserPidByPage(int page);

    public void syncJDPid();

}
