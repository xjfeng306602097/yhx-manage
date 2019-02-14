package com.yhx.user.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yhx.common.constant.JDApiSuffix;
import com.yhx.common.exception.BaseException;
import com.yhx.common.service.impl.BaseServiceImpl;
import com.yhx.common.util.HttpUtil;
import com.yhx.sys.service.SysParameterService;
import com.yhx.user.dao.UserPidMapper;
import com.yhx.user.model.UserPid;
import com.yhx.user.service.UserPidService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        for (int i = start; i <= end; i++) {
            pidNames += (namePrefix + "_" + i + ",");
            if (i % 50 == 0) {
                pidNames = addPid(unionId, authKey, siteId, pidNames, url);
            }
        }
        if (StringUtils.isNotEmpty(pidNames)) {
            addPid(unionId, authKey, siteId, pidNames, url);
        }
    }

    @Override
    public void syncJDPid() {
        int page = 0;
        boolean isContinue = true;
        while(isContinue) {
            page++;
            try {
                isContinue = syncUserPidByPage(page);
            } catch (Exception e) {
                continue;
            }
        }
    }

    @Override
    public boolean syncUserPidByPage(int page) {
        String unionId = sysParameterService.getParamValue("jd_unionid");
        String authKey = sysParameterService.getParamValue("jd_authkey");
        String url = JDApiSuffix.JD_API_PREFFIX + JDApiSuffix.PID_QUERY_SUFFIX;
        Map<String, String> param = new HashMap<String, String>();
        param.put("unionid", unionId);
        param.put("authkey", authKey);
        param.put("page", String.valueOf(page));
        param.put("pageSize", "50");
        String result = HttpUtil.sendGet(url, param);
        JSONObject resultJo = JSONObject.parseObject(result);
        JSONObject data = resultJo.getJSONObject("data");
        JSONArray array = data.getJSONArray("result");
        if (array.size() == 0) {
            return false;
        }
        List<UserPid> userPids = new ArrayList<UserPid>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject pidItem = array.getJSONObject(i);
            if (pidItem.getIntValue("type") != 5) {
                String id = pidItem.getString("id");
                String siteId = pidItem.getString("siteId");
                UserPid userPid = new UserPid();
                userPid.setPid(unionId + "_" + siteId + "_" + id);
                userPid.setType(1);
                userPids.add(userPid);
            }
        }
        if (userPids.size() != 0) {
            mapper.batchSavePid(userPids);
        }
        return true;
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
