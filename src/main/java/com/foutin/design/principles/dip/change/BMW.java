package com.foutin.design.principles.dip.change;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 17:33
 */
public class BMW implements ICar{

    //宝马车当然也可以开动了
    public void run(){
        System.out.println("宝马汽车开始运行...");
    }
}
