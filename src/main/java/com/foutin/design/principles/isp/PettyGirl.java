package com.foutin.design.principles.isp;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 17:03
 */
public class PettyGirl implements IPettyGirl {
    private String name;

    // 美女都有名字
    public PettyGirl(String name) {
        this.name = name;
    }

    // 脸蛋漂亮
    @Override
    public void goodLooking() {
        System.out.println(this.name + "---脸蛋很漂亮!");
    }

    // 气质要好
    @Override
    public void greatTemperament() {
        System.out.println(this.name + "---气质非常好!");
    }

    // 身材要好
    @Override
    public void niceFigure() {
        System.out.println(this.name + "---身材非常棒!");
    }

}
