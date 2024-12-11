package com.foutin.design.principles.isp;

/**
 * @author f2485
 * @Description
 * @date 2023/10/10 17:03
 */
public abstract class AbstractSearcher {
    protected IPettyGirl pettyGirl;

    protected AbstractSearcher(IPettyGirl pettyGirl) {
        this.pettyGirl = pettyGirl;
    }

    // 搜索美女，列出美女信息
    public abstract void show();


}
