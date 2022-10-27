package com.apricot.config;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.google.common.collect.Lists;

//@Configuration
public class FilterConfig {
	/**
     * 基础过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> baseFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebHttpServlet());
        filterRegistrationBean.setUrlPatterns(Lists.newArrayList("/*"));
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
