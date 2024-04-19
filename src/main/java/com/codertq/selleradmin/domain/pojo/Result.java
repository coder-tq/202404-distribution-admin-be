package com.codertq.selleradmin.domain.pojo;

import lombok.Data;

/**
 * author: coder_tq
 * date: 2024/4/14
 */
@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(0, "success", data);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(-1, message);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message);
    }

    public static <T> Result<T> fail(int code, String message, T data) {
        return new Result<>(code, message, data);
    }
}
