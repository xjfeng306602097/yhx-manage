package com.yhx.common.util;

import java.util.UUID;

public class UUIDUtil {
	/**
	 * <p>Title: getKeys</p>  
	 * <p>Description:获取随机生成的32位主键 </p>  
	 * @return
	 */
	public static String getKeys(){
		String uuid = UUID.randomUUID().toString();
		return uuid.replace("-", "");
	}
}
