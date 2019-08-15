package com.dynamic.constans;

public enum TaskCronConstants {


    ADD_SUCCESS(200L, "添加成功"),ADD_FAIL(400L, "添加失败"),
    UPD_SUCCESS(200L, "修改成功"),UPD_FAIL(400L, "修改失败"),
    DEL_SUCCESS(200L, "删除成功"),DEL_FAIL(400L, "删除失败"),
    EXC_SUCCESS(200L, "执行成功"), EXC_FAIL(400L, "执行失败"),
    GET_SUCCESS(200L, "获取成功"), GET_FAIL(400L, "获取失败"),
    RESET_SUCCESS(200L, "重置成功"), RESET_FAIL(400L, "重置失败");

    TaskCronConstants(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    private Long code;
    private String message;
    public Long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
