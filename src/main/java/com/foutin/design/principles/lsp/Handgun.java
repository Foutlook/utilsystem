package com.foutin.design.principles.lsp;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 9:28
 */
public class Handgun extends AbstractGun{

    //手枪的特点是携带方便，射程短
    @Override
    public void shoot() {
        System.out.println("手枪射击...");
    }
}
