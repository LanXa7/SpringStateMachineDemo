package com.example.config

import com.example.entity.Order
import com.example.entity.enums.order.OrderEvents
import com.example.entity.enums.order.OrderStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.StateMachineContext
import org.springframework.statemachine.StateMachinePersist
import org.springframework.statemachine.config.EnableStateMachine
import org.springframework.statemachine.config.StateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer
import org.springframework.statemachine.persist.DefaultStateMachinePersister
import org.springframework.statemachine.support.DefaultStateMachineContext

@Configuration
@EnableStateMachine(name = ["orderStateMachine"])
class OrderStateMachineConfig : StateMachineConfigurerAdapter<OrderStatus, OrderEvents>() {

    /**
     * 配置状态
     */
    override fun configure(states: StateMachineStateConfigurer<OrderStatus, OrderEvents>) {
        states.withStates()
            .initial(OrderStatus.INIT)
            .states(setOf(OrderStatus.INIT, OrderStatus.PAYED, OrderStatus.SHIPPED, OrderStatus.RECEIVED))
    }

    /**
     * 配置状态转换事件关系
     */
    override fun configure(transitions: StateMachineTransitionConfigurer<OrderStatus, OrderEvents>) {
        transitions
            .withExternal()
            .source(OrderStatus.INIT).target(OrderStatus.PAYED)
            .event(OrderEvents.PAY)
            .and()
            .withExternal()
            .source(OrderStatus.PAYED).target(OrderStatus.SHIPPED)
            .event(OrderEvents.SHIPPING)
            .and()
            .withExternal()
            .source(OrderStatus.SHIPPED).target(OrderStatus.RECEIVED)
            .event(OrderEvents.RECEIVED)
    }

    /**
     * 持久化配置
     * 在实际使用中 可以配合Redis等进行持久化操作
     */
    @Bean
    fun persister(): DefaultStateMachinePersister<OrderStatus, OrderEvents, Order> {
        return DefaultStateMachinePersister(
            object : StateMachinePersist<OrderStatus, OrderEvents, Order> {

                override fun write(
                    context: StateMachineContext<OrderStatus, OrderEvents>,
                    order: Order
                ) {
                    // 此处并没有进行持久化操作
                }

                override fun read(order: Order): StateMachineContext<OrderStatus, OrderEvents> {
                    // 此处直接获取order中的状态，其实并没有进行持久化读取操作
                    return DefaultStateMachineContext(
                        order.state,
                        null,
                        null,
                        null
                    )
                }
            }
        )
    }

}