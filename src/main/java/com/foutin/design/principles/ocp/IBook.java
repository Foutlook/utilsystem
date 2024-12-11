package com.foutin.design.principles.ocp;

/**
 * @author f2485
 * @Description
 * @date 2023/10/9 20:23
 */
public interface IBook {

    //书籍有名称
    String getName();
    //书籍有售价
    int getPrice();
    //书籍有作者
    String getAuthor();

}
