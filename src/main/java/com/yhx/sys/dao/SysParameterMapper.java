package com.yhx.sys.dao;

import com.yhx.sys.model.SysParameter;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface SysParameterMapper extends Mapper<SysParameter> {

    SysParameter selectParam(@Param("paramName") String paramName);

}