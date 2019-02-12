package com.yhx.manage.sys.dao;

import com.yhx.manage.sys.model.SysParameter;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface SysParameterMapper extends Mapper<SysParameter> {

    SysParameter selectParam(@Param("paramName") String paramName);

}