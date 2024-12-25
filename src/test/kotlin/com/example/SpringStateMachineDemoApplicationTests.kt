package com.example

import com.example.entity.Order
import com.example.entity.enums.order.OrderEvents
import com.example.entity.enums.order.OrderStatus
import com.example.ext.logger
import com.example.processor.OrderProcessor
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpringStateMachineDemoApplicationTests {

    @Resource
    private lateinit var orderProcessor: OrderProcessor

    private val log = logger

    @Test
    fun contextLoads() {

    }

    @Test
    fun pay() {
        val order = Order(OrderStatus.INIT)
        orderProcessor.process(order, OrderEvents.PAY)
    }

    @Test
    fun shipping() {
        val order = Order(OrderStatus.PAYED)
        orderProcessor.process(order, OrderEvents.SHIPPING)
    }

    @Test
    fun receive() {
        val order = Order(OrderStatus.SHIPPED)
        orderProcessor.process(order, OrderEvents.RECEIVED)
    }

    @Test
    fun invalidOrder() {
        val order = Order(OrderStatus.PAYED)
        val process = orderProcessor.process(order, OrderEvents.RECEIVED)
        log.info { "状态机接收的状态: $process" }
    }

}
