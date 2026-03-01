package com.usedcar.common.constant;

public final class CommonConstant {

    private CommonConstant() {
    }

    // 用户类型
    public static final String USER_TYPE_ADMIN = "ADMIN";
    public static final String USER_TYPE_MP = "MP_USER";

    // 角色
    public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_SALES = "SALES";

    // 车辆状态
    public static final int VEHICLE_STATUS_DRAFT = 1;
    public static final int VEHICLE_STATUS_ON_SALE = 2;
    public static final int VEHICLE_STATUS_OFF_SHELF = 3;
    public static final int VEHICLE_STATUS_SOLD = 4;

    // 订单状态
    public static final int ORDER_STATUS_PENDING = 1;
    public static final int ORDER_STATUS_TO_PAY = 2;
    public static final int ORDER_STATUS_PAID = 3;
    public static final int ORDER_STATUS_DELIVERED = 4;
    public static final int ORDER_STATUS_COMPLETED = 5;
    public static final int ORDER_STATUS_CANCELLED = 6;

    // 客户阶段
    public static final int CUSTOMER_STAGE_FIRST_CONTACT = 1;
    public static final int CUSTOMER_STAGE_DEMAND_CONFIRM = 2;
    public static final int CUSTOMER_STAGE_PROPOSAL = 3;
    public static final int CUSTOMER_STAGE_NEGOTIATION = 4;
    public static final int CUSTOMER_STAGE_DEAL = 5;
    public static final int CUSTOMER_STAGE_LOST = 6;

    // Redis Key 前缀
    public static final String REDIS_KEY_TOKEN_BLACKLIST = "token:blacklist:";
    public static final String REDIS_KEY_VEHICLE_DETAIL = "vehicle:detail:";
    public static final String REDIS_KEY_VEHICLE_HOT = "vehicle:hot";
    public static final String REDIS_KEY_SEARCH_HOT = "search:hot";

    // 分页默认值
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
}
