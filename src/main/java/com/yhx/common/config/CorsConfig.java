/**
 * 
 */
package com.yhx.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**  
* <p>Title: CorsConfig</p>  
* <p>Description: </p>  
* @author gaolinlou  
* @date 2018年8月16日  
*/
@Configuration
public class CorsConfig {

	@Bean
    public FilterRegistrationBean corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setMaxAge(3600L);

        //2.添加映射路径
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //3.返回新的CorsFilter并设置优先级为0，优先登陆过滤器
        CorsFilter corsFilter = new CorsFilter(configSource);
        FilterRegistrationBean bean = new FilterRegistrationBean(corsFilter);
        bean.setOrder(0);
        return bean;
    }

}
