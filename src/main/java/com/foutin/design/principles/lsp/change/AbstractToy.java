package com.foutin.design.principles.lsp.change;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 18:28
 */
public abstract class AbstractToy {

    // 可以在AbstractToy中声明将声音、形状都委托给AbstractGun处理，仿真枪嘛，形
    // 状和声音都要和真实的枪一样了，然后两个基类下的子类自由延展，互不影响。
    public abstract void play();
}
