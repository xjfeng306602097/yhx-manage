package com.yhx.manage.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yhx.manage.common.constant.JDApiSuffix;
import com.yhx.manage.common.exception.BaseException;
import com.yhx.manage.common.service.impl.BaseServiceImpl;
import com.yhx.manage.common.util.HttpUtil;
import com.yhx.manage.sys.service.SysParameterService;
import com.yhx.manage.user.dao.UserPidMapper;
import com.yhx.manage.user.model.UserPid;
import com.yhx.manage.user.service.UserPidService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserPidServiceImpl extends BaseServiceImpl<UserPidMapper, UserPid> implements UserPidService {

    @Autowired
    private SysParameterService sysParameterService;

    @Override
    public void createUserPid(String namePrefix, int start, int end) {
        String unionId = sysParameterService.getParamValue("jd_unionid");
        String authKey = sysParameterService.getParamValue("jd_authkey");
        String siteId = sysParameterService.getParamValue("jd_siteid");
        String pidNames = "";
        String url = JDApiSuffix.JD_API_PREFFIX + JDApiSuffix.CREATE_PID_SUFFIX;
        for (int i = 1; i <= end - start; i++) {
            pidNames += (namePrefix + "_" + i + ",");
            if (i % 50 == 0) {
                pidNames = addPid(unionId, authKey, siteId, pidNames, url);
            }
        }
        if (StringUtils.isNotEmpty(pidNames)) {
            addPid(unionId, authKey, siteId, pidNames, url);
        }
    }

    private String addPid(String unionId, String authKey, String siteId, String pidNames, String url) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("unionid", unionId);
        param.put("authkey", authKey);
        param.put("pidname", pidNames.substring(0, pidNames.length() - 1));
        param.put("siteid", siteId);
        param.put("type", "3");
        String result = HttpUtil.sendGet(url, param);
        JSONObject resultJo = JSONObject.parseObject(result);
        if (resultJo.getIntValue("error") == 0) {
            pidNames = "";
        } else {
            log.error("UserPidServiceImpl>createUserPid error, msg:" + resultJo.getString("msg"));
            throw new BaseException(204, "UserPidServiceImpl>createUserPid error, msg:" + resultJo.getString("msg"));
        }
        return pidNames;
    }

}
