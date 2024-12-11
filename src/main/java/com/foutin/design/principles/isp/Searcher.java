package com.foutin.design.principles.isp;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 17:04
 */
public class Searcher extends AbstractSearcher {

    public Searcher(IPettyGirl pettyGirl) {
        super(pettyGirl);
    }

    // 展示美女的信息
    public void show() {
        System.out.println("--------美女的信息如下：---------------");
        // 展示面容
        super.pettyGirl.goodLooking();
        // 展示身材
        super.pettyGirl.niceFigure();
        // 展示气质
        super.pettyGirl.greatTemperament();
    }
}
