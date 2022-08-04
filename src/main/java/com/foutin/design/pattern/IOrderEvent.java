package com.foutin.design.pattern;

/**
 * @author xingkai.fan
 * @description 订单时间接口
 * @date 2022/8/4
 */
public interface IOrderEvent {

    /**
     * 待支付
     *
     * @param context
     */
    //void toPay(OrderStatusMachine context) throws Exception;

    /**
     * 预支付
     *
     * @param context
     */
    //void prepayment(OrderStatusMachine context) throws Exception;

    /**
     * 订单未支付超时
     *
     * @param context
     */
    void orderTimeout(OrderStatusMachine context) throws Exception;

    /**
     * 支付成功
     *
     * @param context
     */
    void paySuccess(OrderStatusMachine context) throws Exception;

    /**
     * 取消支付
     *
     * @param context
     */
    void cancelPay(OrderStatusMachine context) throws Exception;

    /**
     * 完成订单
     *
     * @param context
     */
    void finishOrder(OrderStatusMachine context) throws Exception;

    /**
     * 申请退款
     *
     * @param context
     */
    void applyRefund(OrderStatusMachine context) throws Exception;

    /**
     * 确认退款
     *
     * @param context
     */
    void confirmRefund(OrderStatusMachine context) throws Exception;

    /**
     * 拒绝退款
     *
     * @param context
     */
    void refuseRefund(OrderStatusMachine context) throws Exception;

    /**
     * 取消退款
     * @param context
     * @throws Exception
     */
    void cancelRefund(OrderStatusMachine context) throws Exception;
}
