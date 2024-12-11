package com.foutin.design.principles.dip;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 17:30
 */
public class Client {

    public static void main(String[] args) {
        Driver zhangSan = new Driver();
        Benz benz = new Benz();
        // 张三开奔驰车
        zhangSan.drive(benz);
    }

}
