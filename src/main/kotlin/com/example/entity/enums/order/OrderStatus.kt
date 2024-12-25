package com.example.entity.enums.order

/**
 * 订单状态
 */
enum class OrderStatus {
    /**
     * 初始化
     */
    INIT,
    /**
     * 已经支付
     */
    PAYED,
    /**
     * 已经发货
     */
    SHIPPED,
    /**
     * 已经收货
     */
    RECEIVED;
}