package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    /**
     * 用户提交订单
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);



    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 开始进行分页查询
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQuery(int page, int pageSize, Integer status);

    /**
     * 根据订单的id查询信息
     * @param id
     * @return
     */
    OrderVO list(Long id);

    /**
     * 取消订单
     * @param id
     */
    void cancelOrder(Long id) throws Exception;

    /**
     * 再来一单
     * @param id
     */
    void repetiton(Long id);

    /**
     * 商家进行订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult search(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 统计订单的数量
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     *
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 商家取消订单
     *
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;


    /**
     * 派送订单
     *
     * @param id
     */
    void delivery(Long id);

    /**
     * 完成订单
     *
     * @param id
     */
    void complete(Long id);
}
