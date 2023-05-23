//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.wordtest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(
    value = "响应结果",
    description = "接口响应数据"
)
public class Result<T> implements Serializable {
    @ApiModelProperty("承载数据")
    private T data;
    @ApiModelProperty("状态码：0 正常；-1 失败、异常、未知错误；1000 服务器内部异常；1001 服务不可用；10001 用户名或密码错误；10002 用户未登录；10003 令牌失效或不合法；10005 账号或密码为空")
    private int code;
    @ApiModelProperty("状态说明")
    private String msg;
    @ApiModelProperty("服务响应时间")
    private long serverTime;
    @ApiModelProperty("响应状态：code 零成功；非零失败")
    private boolean success = true;

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result() {
        new Result(0, "", (Object)null, System.currentTimeMillis());
    }

    public Result(int errCode, String message) {
        new Result(errCode, message, (Object)null, System.currentTimeMillis());
    }

    public Result(int errCode, String message, T content, long time) {
        this.code = errCode;
        this.msg = message;
        this.data = content;
        this.serverTime = time;
    }

    public boolean hasPresent() {
        return this.data != null;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getServerTime() {
        return this.serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public boolean isSuccess() {
        this.success = 0 == this.code;
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static <T> Result<T> success() {
        return status(null, 0, "success");
    }

    public static Result<Boolean> ok() {
        return status(true, 0, "success");
    }

    public static <T> Result<T> success(String msg) {
        return status(null, 0, msg);
    }

    public static <T> Result<T> ok(T data) {
        return status(data, 0, "success");
    }

    public static <T> Result<T> status(T data, int code, String msg) {
        return status(data, code, msg, System.currentTimeMillis());
    }

    public static <T> Result<T> fail() {
        return fail(-1, "error");
    }

    public static <T> Result<T> fail(String msg) {
        return status(null, -1, msg);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return status(null, code, msg);
    }

    public static <T> Result<T> status(T data, int code, String msg, long serverTime) {
        Result<T> result = new Result();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        result.setServerTime(serverTime);
        return result;
    }

    public String toString() {
        return "Result{data=" + this.data + ", code=" + this.code + ", msg='" + this.msg + '\'' + ", serverTime=" + this.serverTime + '}';
    }
}
