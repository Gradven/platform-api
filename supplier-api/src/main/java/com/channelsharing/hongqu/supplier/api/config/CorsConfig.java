package com.channelsharing.hongqu.supplier.api.config;

import com.channelsharing.common.yaml.YamlProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Created by liuhangjun on 2017/8/29.
 */
@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		
		config.addAllowedOrigin("*");

		/*// 设置你要允许的网站域名，如果全允许则设为 *
		String springProfilesActive = YamlProperties.getSpringProfilesActive();
		
		if (springProfilesActive != null && "prod".equals(springProfilesActive)) {
			// 这里暂时为* 以后需要改成域名
			config.addAllowedOrigin("www.xxxx.com");
		} else {
			config.addAllowedOrigin("*");
		}*/

		// 如果要限制 HEADER 或 METHOD 请自行更改
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}
