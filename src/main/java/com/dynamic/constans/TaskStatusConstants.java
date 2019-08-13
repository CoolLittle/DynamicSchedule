package com.dynamic.constans;

/**
 * @description:
 * @author:
 * @date: 2019/8/13 11:42
 */
public enum  TaskStatusConstants {
	未执行(0),
	执行(1);
	int value;
	TaskStatusConstants(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
