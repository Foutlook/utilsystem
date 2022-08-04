package com.foutin.design.pattern;

import org.springframework.stereotype.Component;

/**
 * @author xingkai.fan
 * @description
 * @date 2022/8/4 17:02
 **/
@Component
public class ToPayOrder extends AbstractIOrderState {
    private static final ToPayOrder instance = new ToPayOrder();

    public static ToPayOrder getInstance() {
        return instance;
    }

    @Override
    public void orderTimeout(OrderStatusMachine context) throws Exception {

    }

    @Override
    public void paySuccess(OrderStatusMachine context) throws Exception {

    }

    @Override
    public void cancelPay(OrderStatusMachine context) throws Exception {

    }

    @Override
    public String getState() {
        return OrderStatusEnum.TO_PAY.getValue();
    }
}
