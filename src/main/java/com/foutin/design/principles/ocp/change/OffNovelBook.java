package com.foutin.design.principles.ocp.change;

import com.foutin.design.principles.ocp.NovelBook;

/**
 * @author f2485
 * @Description
 * @date 2023/10/9 20:28
 */
public class OffNovelBook extends NovelBook {

    public OffNovelBook(String name, int price, String author) {
        super(name, price, author);
    }

    // 覆写销售价格
    @Override
    public int getPrice() {
        // 原价
        int selfPrice = super.getPrice();
        int offPrice = 0;
        if (selfPrice > 4000) { // 原价大于40元，则打9折
            offPrice = selfPrice * 90 / 100;
        } else {
            offPrice = selfPrice * 80 / 100;
        }
        return offPrice;
    }
}
