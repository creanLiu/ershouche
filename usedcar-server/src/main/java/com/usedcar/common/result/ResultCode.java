package com.usedcar.common.result;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),

    // 认证相关 4xx
    UNAUTHORIZED(401, "未登录或Token已过期"),
    FORBIDDEN(403, "权限不足"),
    TOKEN_EXPIRED(401, "Token已过期"),
    TOKEN_INVALID(401, "Token无效"),

    // 参数相关
    PARAM_ERROR(400, "参数错误"),
    PARAM_MISSING(400, "缺少必要参数"),

    // 业务相关
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    VEHICLE_NOT_FOUND(2001, "车辆不存在"),
    ORDER_NOT_FOUND(3001, "订单不存在"),
    CUSTOMER_NOT_FOUND(4001, "客户不存在"),

    // 微信相关
    WX_LOGIN_FAIL(5001, "微信登录失败"),
    WX_PHONE_FAIL(5002, "获取手机号失败");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
