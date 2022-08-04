package com.foutin.design.pattern;

import org.springframework.stereotype.Component;

/**
 * @description
 * @author xingkai.fan
 * @date 2022/8/4 17:04
 **/
@Component
public class PrepaymentOrder extends AbstractIOrderState{


    @Override
    public void paySuccess(OrderStatusMachine context) throws Exception {

    }

    @Override
    public String getState() {
        return OrderStatusEnum.PREPAYMENT.getValue();
    }
}
