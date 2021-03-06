package com.yhx.sys.service.impl;

import com.yhx.common.service.impl.BaseServiceImpl;
import com.yhx.sys.dao.SysParameterMapper;
import com.yhx.sys.model.SysParameter;
import com.yhx.sys.service.SysParameterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SysParameterServiceImpl extends BaseServiceImpl<SysParameterMapper, SysParameter> implements SysParameterService {

    @Override
    public SysParameter selectParam(String paramName) {
        return mapper.selectParam(paramName);
    }

    // "sys:param:xxxxxx"
    @Override
    @Cacheable(value = "sys", key = "'param:'+#paramName")
    public String getParamValue(String paramName) {
        SysParameter param = this.selectParam(paramName);
        return param != null ? param.getParamValue() : null;
    }

    @Override
    public String getParamValue(String paramName, String defaultValue) {
        String value = this.getParamValue(paramName);
        return value != null && StringUtils.isNotEmpty(value) ? value : defaultValue;
    }


}
