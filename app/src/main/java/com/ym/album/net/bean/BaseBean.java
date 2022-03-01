package com.ym.album.net.bean;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/2/28 20:38
 */
public class BaseBean<T> {
    private String msg;
    private int code;
    private T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
