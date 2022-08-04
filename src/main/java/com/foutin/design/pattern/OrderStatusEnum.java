package com.foutin.design.pattern;

/**
 * 订单状态
 *
 * @author 王培
 * @create 2017-08-03 10:45
 **/
public enum OrderStatusEnum {

    TO_PAY("0", "待支付"),
    PAYED("1", "已支付"),
    CANCEL("2", "订单关闭"),
    COMPLETE("3", "交易完成"),
    CONFIRMED("4", "已确认"),
    PREPAYMENT("5", "预支付"),
    REFUND("6", "退款中"),

    ;

    /**
     * 枚举值
     */
    private String value;

    /**
     * 描述
     */
    private String desc;

    private OrderStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderStatusEnum getEnum(String value) {
        OrderStatusEnum resultEnum = null;
        OrderStatusEnum[] enumAry = OrderStatusEnum.values();
        for (int i = 0; i < enumAry.length; i++) {
            if (enumAry[i].getValue().equals(value)) {
                resultEnum = enumAry[i];
                break;
            }
        }
        return resultEnum;
    }

}
