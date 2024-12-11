package com.foutin.design.principles.dip.change;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 17:32
 */
public class Driver implements IDriver{

    //司机的主要职责就是驾驶汽车
    public void drive(ICar car){
        car.run();
    }
}
