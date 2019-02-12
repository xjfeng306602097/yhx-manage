package com.yhx.manage.user.service;

import com.yhx.manage.common.service.BaseService;
import com.yhx.manage.user.model.UserPid;

public interface UserPidService extends BaseService<UserPid> {

    public void createUserPid(String namePrefix, int start, int end);

}
