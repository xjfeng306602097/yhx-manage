package com.yhx.sys.service;

import com.yhx.common.service.BaseService;
import com.yhx.sys.model.SysParameter;

public interface SysParameterService extends BaseService<SysParameter> {

    SysParameter selectParam(String paramName);

    String getParamValue(String paramName);

    String getParamValue(String paramName, String defaultValue);

}
