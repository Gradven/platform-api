package com.channelsharing.hongqu.supplier.api.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger接口自动生成工具的配置
 *
 * @author liuhangjun
 * @date 2017-01-22
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig  extends WebMvcConfigurerAdapter {
	
	private String basePackage = "com.channelsharing.hongqu.supplier.api.controller";

	@Bean
	public Docket api() {

		Parameter debugUserIdParam = new ParameterBuilder().name("debugUserId").required(false).parameterType("query") //参数类型支持header, cookie, body, query etc
				.allowMultiple(false).defaultValue("1").modelRef(new ModelRef("string")).description("用于调试需要登录的接口")
				.build();

		Parameter clientAgentParam = new ParameterBuilder().name("X-Client-Agent").required(false).parameterType("header")
				.allowMultiple(false).defaultValue("X_IOS_750.0*1334.0_2.4.1_1303_WiFi_iPhone7Plus_10.2.1_c83ebb126f60072060a7bd4b6b9cf786_A10_2_2")
				.modelRef(new ModelRef("string")).description("用户UA信息")
				.build();

		return new Docket(DocumentationType.SWAGGER_2)
				.globalOperationParameters(Lists.newArrayList(debugUserIdParam, clientAgentParam)).select()
				.apis(RequestHandlerSelectors.basePackage(basePackage)).build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				// 页面标题
				.title("Hongqu-供应商管理平台接口 使用 Swagger2 构建RESTful API")
				// 创建人
				.contact(new Contact("develop", null, "xxx@xxx.com"))
				// 版本号
				.version("1.0")
				// 描述
				.description("接口展示").build();
	}
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	
	
}
