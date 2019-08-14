package com.dynamic.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @description:
 * @author: caozheng
 * @date: 2019/8/14 14:26
 */
@Slf4j
public class JsonKitTest {

	@Test
	public void parseObject() throws Exception {

		String str = "[{\"Type\":\"String\",\"Value\":\"a1\"}]";

		List<Map<String,Object>> list = JsonKit.parseArray(str);

		Object[] types = new Object[list.size()];
		Object[] values = new Object[list.size()];
		int i = 0;
		for (Map<String,Object> m : list){
			types[i] = BeanUtils.convert(m.get("Type").toString());
			values[i] = m.get("Value");
			i++;
		}

		log.info("{}",list);
	}
}
