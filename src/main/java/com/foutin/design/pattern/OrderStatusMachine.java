package com.foutin.design.pattern;

/**
 * @author xingkai.fan
 * @description 订单状态机----不能用
 * @date 2022/8/4 15:38
 **/
public class OrderStatusMachine {

    private AbstractIOrderState currentState;
    private OrderEventParam orderEventParam;

    public OrderStatusMachine(AbstractIOrderState orderState, OrderEventParam orderEventParam) {
        this.currentState = orderState;
        this.orderEventParam = orderEventParam;
    }

    //public Boolean doAction() throws Exception {
    //    switch (orderEvent) {
    //        case OrderStatusEnum.REFUND:
    //            orderStatus.cancelRefund(this);
    //            break;
    //        case OrderStatusEnum.TO_PAY:
    //            orderStatus.paySuccess(this);
    //            break;
    //        case OrderConstant.CANCELED_ORDER:
    //            orderStatus.canceledOrder(this);
    //            break;
    //        case OrderConstant.FINISH_ORDER:
    //            orderStatus.finishOrder(this);
    //            break;
    //        case OrderConstant.AUTO_FINISH_ORDER:
    //            orderStatus.autoFinishOrder(this);
    //            break;
    //        case OrderConstant.ORDER_TIMEOUT:
    //            orderStatus.orderTimeout(this);
    //            break;
    //        case OrderConstant.PAY_SUCCESS:
    //            orderStatus.paySuccess(this);
    //            break;
    //        case OrderConstant.REFUSE_REFUND:
    //            orderStatus.refuseRefund(this);
    //            break;
    //        case OrderConstant.SERVER_TIME_EDN:
    //            orderStatus.serverTimeEnd(this);
    //            break;
    //        case OrderConstant.START_SERVE:
    //            orderStatus.startServe(this);
    //            break;
    //        case OrderConstant.COMMENT:
    //            orderStatus.comment(this);
    //            break;
    //        case OrderConstant.AUTO_COMMENT:
    //            orderStatus.autoComment(this);
    //            break;
    //        default:
    //            throw new ServiceErrorException("暂不支持此事件");
    //    }
    //    return true;
    //}
}
