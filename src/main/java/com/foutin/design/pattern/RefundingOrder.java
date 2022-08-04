package com.foutin.design.pattern;

import org.springframework.stereotype.Component;

/**
 * @description
 * @author xingkai.fan
 * @date 2022/8/4 17:07
 **/
@Component
public class RefundingOrder extends AbstractIOrderState{

    @Override
    public void confirmRefund(OrderStatusMachine context) throws Exception {

    }

    @Override
    public void refuseRefund(OrderStatusMachine context) throws Exception {

    }

    @Override
    public void cancelRefund(OrderStatusMachine context) throws Exception {

    }

    @Override
    public String getState() {
        return OrderStatusEnum.REFUND.getValue();
    }
}
