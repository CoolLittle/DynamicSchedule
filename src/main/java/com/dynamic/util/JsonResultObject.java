package com.dynamic.util;

public class JsonResultObject<T> extends JsonResult{
    private T data;

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
