package com.dynamic.constans;

public enum TaskCronConstants {


    ADD_SUCCESS(200L, "添加成功"),ADD_FAIL(400L, "添加失败"),
    DEL_SUCCESS(200L, "删除成功"),DEL_FAIL(400L, "删除失败"),
    RESET_SUCCESS(200L, "重ADD置成功"), RESET_FAIL(400L, "重置失败");

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
