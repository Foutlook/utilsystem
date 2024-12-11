package com.foutin.design.principles.ocp;

/**
 * @author f2485
 * @Description
 * @date 2023/10/9 20:23
 */
public class NovelBook implements IBook {
    // 书籍名称
    private String name;
    // 书籍的价格
    private int price;
    // 书籍的作者
    private String author;

    // 通过构造函数传递书籍数据
    public NovelBook(String name, int price, String author) {
        this.name = name;
        this.price = price;
        this.author = author;
    }

    // 获得作者是谁
    @Override
    public String getAuthor() {
        return this.author;
    }

    // 书籍叫什么名字
    @Override
    public String getName() {
        return this.name;
    }

    // 获得书籍的价格
    @Override
    public int getPrice() {
        return this.price;
    }
}
