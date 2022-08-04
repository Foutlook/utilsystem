package com.foutin.design.pattern;

import org.springframework.stereotype.Component;

/**
 * @author xingkai.fan
 * @description
 * @date 2022/8/4 17:05
 **/
@Component
public class PayedOrder extends AbstractIOrderState {

    @Override
    public void finishOrder(OrderStatusMachine context) throws Exception {

    }

    @Override
    public void applyRefund(OrderStatusMachine context) throws Exception {

    }


    @Override
    public String getState() {
        return OrderStatusEnum.PAYED.getValue();
    }
}
