package com.foutin.design.principles.lsp;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 9:29
 */
public class Rifle extends AbstractGun{

    //步枪的特点是射程远，威力大
    public void shoot(){
        System.out.println("步枪射击...");
    }

}
