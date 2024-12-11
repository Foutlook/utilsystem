package com.foutin.design.principles.dip;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 17:30
 */
public class Driver {

    // 司机的主要职责就是驾驶汽车
    public void drive(Benz benz) {
        benz.run();
    }

}
