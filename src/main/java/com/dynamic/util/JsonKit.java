package com.dynamic.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: caozheng
 * @Date: 2019/2/16 18:03
 */
@Slf4j
public class JsonKit {

	private static ObjectMapper mapper = new ObjectMapper();
	/**
	 * 将json形式字符串转换为java实体类
	 * @param jsonStr 参数字符串
	 * @param clazz 目标类
	 * @param <T> 泛型
	 * @return 目标类
	 */
	public static <T> T parseObject(String jsonStr, Class<T> clazz) {
		if(StringUtils.isNotEmpty(jsonStr)) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		T readValue = null;
		try {
			readValue = mapper.readValue(jsonStr.getBytes(), clazz);
			log.info("{}",readValue);
		} catch (Exception e) {
			log.error("Json转化异常:{}",e);
		}
		return readValue;
	}

	/**
	 * 将json形式字符串转换为java实体类
	 * @param jsonStr 参数字符串
	 * @return 目标类
	 */
	public static List parseArray(String jsonStr) {
		if(StringUtils.isEmpty(jsonStr)) {
			return null;
		}
		List<Map> readValue = null;
		try {
			readValue = mapper.readValue(jsonStr.getBytes(), new TypeReference<List<Map<String,Object>>>(){});
		} catch (Exception e) {
			log.error("Json转化异常:{}",e);
		}
		return readValue;
	}
}


