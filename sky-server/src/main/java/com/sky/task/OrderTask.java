package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 1 * * * ? *")
    public void TimeOutOrder() {
        log.info("定时处理未支付的订单：{}", LocalDateTime.now());
        //查询订单的数据
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> orders = orderMapper.getByStatusAndTimeOut(Orders.PENDING_PAYMENT, time);

        //修改订单数据
        if (orders != null && orders.size() > 0) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("订单超时，已取消");

                orderMapper.update(order);
            }
        }
    }
    @Scheduled(cron = "0 0 1 * * ? *")
    public void processDeliveryOrder(){
        log.info("定时处理已经派送的订单");
        //查询订单的数据
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orders = orderMapper.getByStatusAndTimeOut(Orders.DELIVERY_IN_PROGRESS, time);

        //修改订单数据
        if (orders != null && orders.size() > 0) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("订单已派送，已取消");
                orderMapper.update(order);
            }
        }
    }
}
