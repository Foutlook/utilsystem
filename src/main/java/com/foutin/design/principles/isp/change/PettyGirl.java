package com.foutin.design.principles.isp.change;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 17:09
 */
public class PettyGirl implements IGoodBodyGirl, IGreatTemperamentGirl {

    private String name;

    // 美女都有名字
    public PettyGirl(String name) {
        this.name = name;
    }

    // 脸蛋漂亮
    public void goodLooking() {
        System.out.println(this.name + "---脸蛋很漂亮!");
    }

    // 气质要好
    public void greatTemperament() {
        System.out.println(this.name + "---气质非常好!");
    }

    // 身材要好
    public void niceFigure() {
        System.out.println(this.name + "---身材非常棒!");
    }


    // 以上把一个臃肿的接口变更为两个独立的接口所依赖的原则就是接口隔离原则，让星探
    // AbstractSearcher依赖两个专用的接口比依赖一个综合的接口要灵活。接口是我们设计时对外
    // 提供的契约，通过分散定义多个接口，可以预防未来变更的扩散，提高系统的灵活性和可维
    // 护性

}
