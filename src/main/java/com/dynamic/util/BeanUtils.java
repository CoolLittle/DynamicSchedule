package com.dynamic.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Optional;

public class BeanUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 查找公有域
     *
     * @param clazz 类对象
     * @param name  域名称
     * @return Field
     */
    public static Field findField(Class<?> clazz, String name) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException ex) {
            return findDeclaredField(clazz, name);
        }
    }

    /**
     * 查找所有域
     *
     * @param clazz 类对象
     * @param name  域名称
     * @return Field
     */
    public static Field findDeclaredField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null) {
                return findDeclaredField(clazz.getSuperclass(), name);
            }
            return null;
        }
    }

    /**
     * 查找某个域的值
     *
     * @param obj  对象
     * @param name 域名称
     * @return Object
     * @throws NoSuchFieldException
     */
    public static Object getProperty(Object obj, String name) throws NoSuchFieldException {
        Object value = null;
        Field field = findField(obj.getClass(), name);
        if (field == null) {
            String message = "no such field [".concat(name).concat("]");
            LOGGER.error(message);
            throw new NoSuchFieldException(message);
        }
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            value = field.get(obj);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
        field.setAccessible(accessible);
        return value;
    }

    /**
     * 实例化对象
     *
     * @param clazz 类对象
     * @return Optional<Object>
     */
    public static Optional<Object> newInstance(Class<?> clazz,Object ... param) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        constructors[0].setAccessible(true);
        try {
			return Optional.of(constructors[0].newInstance(param));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * 设置域值
     *
     * @param target    目标对象
     * @param fieldName 域名称
     * @param value     域目标值
     */
    public static void setFieldValue(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static Class convert(String str){

		if("String".equals(str)|| "string".equals(str)){
			return String.class;
		}else if("Integer".equals(str)){
			return Integer.class;
		}else if("int".equals(str)){
			return int.class;
		}else if("Boolean".equals(str)){
			return Boolean.class;
		}else if("boolean".equals(str)){
			return boolean.class;
		}else{
			return null;
		}
	}
}
