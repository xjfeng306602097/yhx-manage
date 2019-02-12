package com.yhx.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
* Description 
* <p>value书写格式 n_n，匹配规则为小于等于n_n最近的一个版本，示例如下：
 * 现有接口 v1.0,v1.1,v2.0,v2.3
 * 访问 v1.1/xxx   执行： v1.1
 * 访问 v1.2/xxx   执行： v1.1
 * 访问 v2.1/xxx   执行： v2.0
 * 访问 v4.0/xxx   执行： v2.3
</p>
* @author xiaojunfeng
* dateTime 2019年1月31日 下午2:50:19
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {

	/**
	* 版本号，格式如 1.1
	*/
    String value();
}