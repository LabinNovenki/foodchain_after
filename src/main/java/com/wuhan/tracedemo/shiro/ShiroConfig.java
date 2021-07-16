package com.wuhan.tracedemo.shiro;


import com.wuhan.tracedemo.filters.JwtFilter;
import com.wuhan.tracedemo.jwt.JwtCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import javax.servlet.Filter;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.*;

@Configuration
public class ShiroConfig {
	/**
	 * 先走 filter ，然后 filter 如果检测到请求头存在 token，则用 token 去 login，走 Realm 去验证
	 */
	@Bean
	public ShiroFilterFactoryBean factory(SecurityManager securityManager) {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

		// 添加自己的过滤器并且取名为jwt
		Map<String, Filter> filterMap = new LinkedHashMap<>();
		//设置我们自定义的JWT过滤器
		filterMap.put("jwt", new JwtFilter());
		factoryBean.setFilters(filterMap);
		factoryBean.setSecurityManager(securityManager);
		// 设置无权限时跳转的 url;
		factoryBean.setUnauthorizedUrl("/unauthorized");
		Map<String, String> filterRuleMap = new HashMap<>();
		// 所有请求通过我们自己的JWT Filter
		filterRuleMap.put("/**", "jwt");
		// 访问 /unauthorized/** 不通过JWTFilter
		filterRuleMap.put("/unauthorized/**", "anon");
		filterRuleMap.put("/login", "anon");
		filterRuleMap.put("/merchantlogin", "anon");
		filterRuleMap.put("/commentCode/getComment", "anon");
		filterRuleMap.put("/commentCode/commitComment", "anon");
		// 可匿名访问

		// swagger
		filterRuleMap.put("/swagger**/**", "anon");
		filterRuleMap.put("/webjars/**", "anon");
		filterRuleMap.put("/v2/**", "anon");

		factoryBean.setFilterChainDefinitionMap(filterRuleMap);
		return factoryBean;
	}

	@Bean
	JWTShiroRealm jwtShiroRealm() {
		JWTShiroRealm jwtRealm = new JWTShiroRealm();
		// 设置加密算法
		CredentialsMatcher credentialsMatcher = new JwtCredentialsMatcher();
		// 设置加密次数
		jwtRealm.setCredentialsMatcher(credentialsMatcher);
		return jwtRealm;
	}

	/**
	 * 注入 securityManager
	 */
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置自定义 realm.
		securityManager.setRealm(jwtShiroRealm());

		/*
		 * 关闭shiro自带的session，详情见文档
		 * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
		 */
		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		securityManager.setSubjectDAO(subjectDAO);
		return securityManager;
	}

	/**
	 * 添加注解支持
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		// 强制使用cglib，防止重复代理和可能引起代理出错的问题
		// https://zhuanlan.zhihu.com/p/29161098
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
}
