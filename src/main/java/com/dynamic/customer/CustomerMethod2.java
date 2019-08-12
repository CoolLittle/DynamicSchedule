package com.dynamic.customer;

import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: caozheng
 * @date: 2019/8/12 16:33
 */
@Component
public class CustomerMethod2 {

	public void print(){
		System.out.println("已经注入过的Bean");
	}

}
