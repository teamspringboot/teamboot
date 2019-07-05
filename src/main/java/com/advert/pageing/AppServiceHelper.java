package com.advert.pageing;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import com.advert.exception.GlobalRuntimeException;
import com.advert.mapper.I18nMessage;
@Component
public class AppServiceHelper implements ApplicationContextAware{
	private static ApplicationContext applicationContext;
	private static I18nMessage i18nMessage;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		AppServiceHelper.applicationContext = applicationContext;
	}

	public static Object findBean(String beanId)
			throws NoSuchBeanDefinitionException {
		Object service = applicationContext.getBean(beanId);
		return service;
	}
	
	public static String getMessage(String key, Object[] params, Locale locale) {

		if (locale == null)
			locale = new java.util.Locale("zh", "CN");
		// 从后台代码获取国际化信息
		String i18n = "";
		try {
			i18n = applicationContext.getMessage(key, params, locale);
		} catch (NoSuchMessageException e) {
			e.printStackTrace();
		}
		return i18n;
	}
	
	@SuppressWarnings(value = "all")
	public static Object findBeanOfType(Class clz) {
		if (clz == null) {
			return null;
		}
		Object service = null;
		try {
			Map<String, Object> serviceMap = applicationContext
					.getBeansOfType(clz);
			Iterator<String> beanNames = serviceMap.keySet().iterator();
			while (beanNames.hasNext()) {
				Object instance = serviceMap.get(beanNames.next());
				if (instance.getClass().equals(clz)) {
					service = instance;
				} else if (AopUtils.isAopProxy(instance)) {
					service = instance;
					break;
				}
			}
		} catch (NoSuchBeanDefinitionException ex) {
			throw new GlobalRuntimeException("no such bean for[" + clz + "]",
					ex);
		} catch (BeansException ex) {
			throw new GlobalRuntimeException("bean exception for[" + clz + "]",
					ex);
		}

		return service;
	}

	public static I18nMessage getI18nMessage() {
		if (i18nMessage == null) {
			i18nMessage = (I18nMessage) AppServiceHelper
					.findBean("i18nDBMessage");
		}
		return i18nMessage;
	}
}
