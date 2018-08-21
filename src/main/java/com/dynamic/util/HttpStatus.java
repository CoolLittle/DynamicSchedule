package com.dynamic.util;

public enum  HttpStatus {

        SUCCESS("200"),
        FAILURE("400"),
        UNAUTHOR("401"),
        ERROR("500");

        private final String value;
        HttpStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

}
