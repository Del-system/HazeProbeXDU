package com.k1wamiiro.hazeprobe.entity;

import lombok.Data;

@Data
public class InfoResponse<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> InfoResponse<T> success() {
        InfoResponse<T> response = new InfoResponse<>();
        response.setCode(1);
        response.setMsg("success");
        return response;
    }
}
