package com.foutin.design.pattern;

/**
 * @author xingkai.fan
 * @description
 * @date 2022/8/4 16:35
 **/
public abstract class AbstractIOrderState implements IOrderEvent {

    //@Override
    //public void toPay(OrderStatusMachine context) throws Exception {
    //    throw new Exception("目前订单状态不支持该流转");
    //}

    //@Override
    //public void prepayment(OrderStatusMachine context) throws Exception {
    //    throw new Exception("目前订单状态不支持该流转");
    //}

    @Override
    public void orderTimeout(OrderStatusMachine context) throws Exception {
        throw new Exception("目前订单状态不支持该流转");
    }

    @Override
    public void paySuccess(OrderStatusMachine context) throws Exception {
        throw new Exception("目前订单状态不支持该流转");
    }

    @Override
    public void cancelPay(OrderStatusMachine context) throws Exception {
        throw new Exception("目前订单状态不支持该流转");
    }

    @Override
    public void finishOrder(OrderStatusMachine context) throws Exception {
        throw new Exception("目前订单状态不支持该流转");
    }

    @Override
    public void applyRefund(OrderStatusMachine context) throws Exception {
        throw new Exception("目前订单状态不支持该流转");
    }

    @Override
    public void confirmRefund(OrderStatusMachine context) throws Exception {
        throw new Exception("目前订单状态不支持该流转");
    }

    @Override
    public void refuseRefund(OrderStatusMachine context) throws Exception {
        throw new Exception("目前订单状态不支持该流转");
    }

    @Override
    public void cancelRefund(OrderStatusMachine context) throws Exception {
        throw new Exception("目前订单状态不支持该流转");
    }

    /**
     * 获取当前状态
     * @return
     */
    public abstract String getState();
}
