package com.foutin.design.principles.dip.change;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 17:33
 */
public class Client {

    public static void main(String[] args) {
        IDriver zhangSan = new Driver();
        ICar benz = new Benz();
//张三开奔驰车
        zhangSan.drive(benz);
    }
}
