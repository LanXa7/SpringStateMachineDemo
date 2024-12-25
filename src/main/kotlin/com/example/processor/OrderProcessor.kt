package com.example.processor

import com.example.entity.Order
import com.example.entity.enums.order.OrderEvents
import com.example.entity.enums.order.OrderStatus
import com.example.ext.logger
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.StateMachineEventResult.ResultType
import org.springframework.statemachine.persist.StateMachinePersister
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class OrderProcessor(
    private val orderStateMachine: StateMachine<OrderStatus, OrderEvents>,
    private val persister: StateMachinePersister<OrderStatus, OrderEvents, Order>
) {
    private val log = logger



    fun process(order: Order, event: OrderEvents): Boolean {

        // 构建Message对象，包含事件和扩展状态
        val message = MessageBuilder
            .withPayload(event)
            .setHeader("order", order) // 将订单对象放入消息头部
            .build()

        return sendEvent(message)
    }


    private fun sendEvent(message: Message<OrderEvents>): Boolean {
        val order = message.headers["order"] as Order
        persister.restore(orderStateMachine, order)
        // 发送事件并获取第一个结果
        val result = orderStateMachine.sendEvent(Mono.just(message))
            .blockFirst()// 阻塞并获取第一个结果
        // 检查结果是否为空，并且事件是否被状态机接受
        return result != null && result.resultType == ResultType.ACCEPTED
    }
}