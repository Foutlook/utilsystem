package com.foutin.design.principles.lsp;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 9:30
 */
public class Client {

    public static void main(String[] args) {
        // 产生三毛这个士兵
        Soldier sanMao = new Soldier();
        // 给三毛一支枪
        sanMao.setGun(new Rifle());
        sanMao.killEnemy();


        // 玩具枪
        // Soldier sanMao = new Soldier();
        // sanMao.setGun(new ToyGun());
        // sanMao.killEnemy();

    }
}
