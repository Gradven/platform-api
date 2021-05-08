package com.channelsharing.common.yaml;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/**
 * Created by liuhangjun on 2017/7/17.
 */
@Slf4j
public class YamlProperties {
	
	private static String springProfilesActive;

	/**
	 * 优先从启动命令行中读取环境变量 如果读取不到，再从application.yml中读取程序的profile环境变量
	 *
	 * @return
	 */
	public static String getSpringProfilesActive() {

		if (springProfilesActive != null)
			return springProfilesActive;

		String key = "spring.profiles.active";

		String profile = System.getenv(key);
		if (profile == null)
			profile = System.getProperty(key);

		if (profile == null) {
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			if (servletRequestAttributes != null) {
				HttpServletRequest request = servletRequestAttributes.getRequest();
				if (request != null) {
					WebApplicationContext webApplicationContext = WebApplicationContextUtils
							.getWebApplicationContext(request.getServletContext());
					if (webApplicationContext != null) {
						profile = webApplicationContext.getEnvironment().getProperty(key);
					}
				}
			}
		}

		if (profile == null) {

			YamlPropertiesFactoryBean propertiesFactoryBean = new YamlPropertiesFactoryBean();

			Resource resource = new ClassPathResource("config/application.yml");
			propertiesFactoryBean.setResources(resource);
			Properties properties = propertiesFactoryBean.getObject();

			profile = properties.getProperty(key);
		}

		springProfilesActive = profile;
		log.info("=========spring.profiles.active===========: {}", profile);

		return profile;
	}

	/**
	 * 读取yaml文件内容
	 *
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {

		YamlPropertiesFactoryBean propertiesFactoryBean = new YamlPropertiesFactoryBean();

		Resource resource = new ClassPathResource("config/application-" + getSpringProfilesActive() + ".yml");
		propertiesFactoryBean.setResources(resource);
		Properties properties = propertiesFactoryBean.getObject();

		return properties.getProperty(key);

	}


}
