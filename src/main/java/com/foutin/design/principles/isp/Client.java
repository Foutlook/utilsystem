package com.foutin.design.principles.isp;

/**
 * @author f2485
 * @Description 接口隔离原则示例
 * @date 2023/10/10 17:05
 */
public class Client {

    // 搜索并展示美女信息
    public static void main(String[] args) {
        // 定义一个美女
        IPettyGirl yanYan = new PettyGirl("嫣嫣");
        AbstractSearcher searcher = new Searcher(yanYan);
        searcher.show();
    }


    // 我们发现接口IPettyGirl的设计是有缺陷的，过于庞大了，容纳了一些可变
    // 的因素，根据接口隔离原则，星探AbstractSearcher应该依赖于具有部分特质的女孩子，而我
    // 们却把这些特质都封装了起来，放到了一个接口中，封装过度了！
}
