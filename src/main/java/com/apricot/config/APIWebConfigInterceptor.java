package com.apricot.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.common.collect.Lists;

/**
 * 添加拦截器
 * 
 * @author rengq
 *
 */
@Configuration
public class APIWebConfigInterceptor implements WebMvcConfigurer {

	@Bean
	APIInterceptor apiInterceptor() {
		return new APIInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration = registry.addInterceptor(apiInterceptor());

		// 需要拦截
		registration.addPathPatterns("/**");

		List<String> patterns = Lists.newArrayListWithCapacity(32);
		patterns.add("/api/member/**");
		patterns.add("/css/**");
		patterns.add("/image/**");
		patterns.add("/js/**");
		patterns.add("/login.html");
		patterns.add("/register.html");
		patterns.add("/reset.html");
		// 不需要拦截的url
		registration.excludePathPatterns(patterns);
	}
	
	/**
     * springmvc视图解析
     * @Title: viewResolver 
     * @return
     */
    @Bean
    public InternalResourceViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/resources/");
        viewResolver.setSuffix(".html");
        viewResolver.setRedirectHttp10Compatible(false);//这里设置为false
        // viewResolver.setViewClass(JstlView.class); // 这个属性通常并不需要手动配置，高版本的Spring会自动检测
        return viewResolver;
    }
    
    /**
     * 添加访问static权限
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    	WebMvcConfigurer.super.addResourceHandlers(registry);
    }

}
