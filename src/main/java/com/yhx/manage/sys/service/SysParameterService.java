package com.yhx.manage.sys.service;

import com.yhx.manage.common.service.BaseService;
import com.yhx.manage.sys.model.SysParameter;

public interface SysParameterService extends BaseService<SysParameter> {

    SysParameter selectParam(String paramName);

    String getParamValue(String paramName);

    String getParamValue(String paramName, String defaultValue);

}
