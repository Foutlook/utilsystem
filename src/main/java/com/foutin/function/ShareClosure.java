package com.foutin.function;

import java.util.ArrayList;
import java.util.List;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 14:36
 */
public class ShareClosure {
    List<Action> list = new ArrayList<>();

    public void Input() {
        for (int i = 0; i < 10; i++) {
            final int copy = i;
            System.out.println("验证点.....：" + i);
            list.add(new Action() {
                @Override
                public void Run() {
                    System.out.println(copy);
                }
            });
        }
    }

    public void Output() {
        for (Action a : list) {
            a.Run();
        }
    }

    public static void main(String[] args) {
        ShareClosure sc = new ShareClosure();
        sc.Input();
        System.out.println("开始OutPut....");
        sc.Output();

    }
}
