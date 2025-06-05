package com.xiaomi.common.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private T data;

    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> fail() {
        return new Result<>(500, "fail", null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }
}
