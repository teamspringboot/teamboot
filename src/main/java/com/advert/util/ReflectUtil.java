package com.advert.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {
	
	private static final  Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

	public static void setFieldValue(Object target, String fname, Class ftype, Object fvalue) {
		if (target == null || fname == null || "".equals(fname)
				|| (fvalue != null && !ftype.isAssignableFrom(fvalue.getClass()))) {
			return;
		}
		Class clazz = target.getClass();
		try {
			Method method = clazz.getDeclaredMethod(setterMethodName(fname),
					ftype);
			if (!Modifier.isPublic(method.getModifiers())) {
				method.setAccessible(true);
			}
			method.invoke(target, fvalue);

		} catch (Exception me) {
			// if (logger.isDebugEnabled()) {
			// logger.debug(me);
			// }
			try {
				Field field = clazz.getDeclaredField(fname);
				if (!Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
				}
				field.set(target, fvalue);
			} catch (Exception fe) {
				if (logger.isErrorEnabled()) {
					logger.error("Exception",fe);
				}
			}
		}
	}

	/**
	 * 用Reflection机制得到所有属性的Map形式
	 * 
	 * @return
	 * @author xiali2
	 * @since 2008-10-14
	 */
	public static Map<String, Object> getMapFieldData(Object target) {
		Map<String, Object> map = new HashMap<String, Object>();
		Class clazz = target.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getDeclaredMethods();
		for (Field field : fields) {
			String fieldName = field.getName();
			if ("messageTypeId".equals(fieldName)) {
				continue;
			}
			String getMethod = getterMethodName(fieldName);
			for (Method method : methods) {
				if (method.getName().equals(getMethod)) {
					try {
						Object ret = method.invoke(target, null);
						if (ret != null) {
							map.put(fieldName, ret);
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return map;
	}

	/**
	 * 得到字段的get方法名
	 * @param fieldName 字段名
	 * @return 字段的get方法名
	 */
	public static String getterMethodName(String fieldName) {
		return "get" + methodSuffix(fieldName);
	}

	/**
	 * 得到字段的set方法名
	 * @param fieldName 字段名
	 * @return 字段的set方法名
	 */
	public static String setterMethodName(String fieldName) {
		return "set" + methodSuffix(fieldName);
	}

	/**
	 * 得到字段get/set方法的后缀
	 * @param fieldName 字段名
	 * @return 字段get/set方法的后缀
	 */
	private static String methodSuffix(String fieldName) {
		StringBuilder sb = new StringBuilder(fieldName);
		if (Character.isLowerCase(sb.charAt(0))) {
			if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
				sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			}
		}
		return sb.toString();
	}
}
