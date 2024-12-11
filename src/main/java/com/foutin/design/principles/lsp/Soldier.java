package com.foutin.design.principles.lsp;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 9:29
 */
public class Soldier {

    // 定义士兵的枪支
    private AbstractGun gun;

    // 给士兵一支枪
    public void setGun(AbstractGun gun) {
        this.gun = gun;
    }

    public void killEnemy() {
        System.out.println("士兵开始杀敌人...");
        gun.shoot();
    }
}
