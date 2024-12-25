package com.example.entity.enums.order

/**
 * 订单状态改变事件
 */
enum class OrderEvents {
    /**
     * 支付
     */
    PAY,

    /**
     * 发货
     */
    SHIPPING,

    /**
     * 确认收货
     */
    RECEIVED;
}