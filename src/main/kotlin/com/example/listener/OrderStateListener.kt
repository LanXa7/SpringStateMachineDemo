package com.example.listener

import com.example.entity.Order
import com.example.entity.enums.order.OrderEvents
import com.example.entity.enums.order.OrderStatus
import com.example.ext.logger
import org.springframework.statemachine.StateContext
import org.springframework.statemachine.annotation.OnTransition
import org.springframework.statemachine.annotation.WithStateMachine
import org.springframework.stereotype.Component

@Component
@WithStateMachine(name = "orderStateMachine")
class OrderStateListener {
    private val log = logger

    @OnTransition(source = ["INIT"], target = ["PAYED"])
    fun pay(context: StateContext<OrderStatus, OrderEvents>): Boolean {
        val order = context.messageHeaders["order"] as Order
        order.state = OrderStatus.PAYED
        log.info { "订单表保存数据" }
        log.info { "发送站内消息" }
        log.info { "回调支付接口" }
        log.info { "同步至数据仓库" }
        return true
    }

    @OnTransition(source = ["PAYED"], target = ["SHIPPED"])
    fun shipping(context: StateContext<OrderStatus, OrderEvents>): Boolean {
        val order = context.messageHeaders["order"] as Order
        order.state = OrderStatus.SHIPPED
        log.info { "创建物流订单" }
        log.info { "更新物流订单状态" }
        log.info { "同步至数据仓库" }
        return true
    }

    @OnTransition(source = ["SHIPPED"], target = ["RECEIVED"])
    fun receive(context: StateContext<OrderStatus, OrderEvents>): Boolean {
        val order = context.messageHeaders["order"] as Order
        order.state = OrderStatus.RECEIVED
        log.info { "更新订单数据" }
        log.info { "同步至数据仓库" }
        return true
    }
}