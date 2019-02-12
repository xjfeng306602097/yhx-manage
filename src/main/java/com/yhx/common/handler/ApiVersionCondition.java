package com.yhx.common.handler;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
	private String apiVersion;
	
    public ApiVersionCondition(String apiVersion){
        this.apiVersion = apiVersion;
    }
    
    public ApiVersionCondition combine(ApiVersionCondition other) {
        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVersionCondition(other.getApiVersion());
    }
    
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        if (compare(getPathInfo(request)) >= 0) {
            return this;
        }
        return null;
    }
    
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // 优先匹配最新的版本号
        return compare("v" + other.getApiVersion() + "/");
    }
    
    public String getApiVersion() {
        return apiVersion;
    }
    
    private String getPathInfo(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && contextPath.length() > 0) {
            uri = uri.substring(contextPath.length());
        }
        return uri;
    }
    
    private int compare(String version) {
        // 路径中版本的前缀， 这里用 /v[0-9].[0-9]/的形式
		Pattern reqPatten = Pattern.compile("v(\\d+)((\\.\\d+)?)/");
		// api版本的前缀， 这里用 [0-9].[0-9]的形式
		Pattern apiPatten = Pattern.compile("(\\d+)((\\.\\d+)?)");
		Matcher reqMatcher = reqPatten.matcher(version);
		Matcher apiMatcher = apiPatten.matcher(this.apiVersion);
		if (reqMatcher.find() && apiMatcher.find()) {
			Integer firstVersion = Integer.valueOf(reqMatcher.group(1));
			Integer firstApiVersion = Integer.valueOf(apiMatcher.group(1));
			// 如果请求的版本号大于配置版本号， 则满足
			if (firstVersion > firstApiVersion) {
				return 1;
			} else if (firstVersion == firstApiVersion) {
				String lastVersionStr = reqMatcher.group(2).replace(".", "");
				String lastApiVersionStr = apiMatcher.group(2).replace(".", "");
				Integer lastVersion = Integer.parseInt("".equals(lastVersionStr) ? "0" : lastVersionStr);
				Integer lastApiVersion = Integer.parseInt("".equals(lastApiVersionStr) ? "0" : lastApiVersionStr);
				if (lastVersion > lastApiVersion) {
					return 1;
				} else if (lastVersion == lastApiVersion) {
					return 0;
				}
			}
		}
		return -1;
    }
}
