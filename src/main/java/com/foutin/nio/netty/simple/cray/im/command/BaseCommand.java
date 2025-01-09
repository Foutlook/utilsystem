package com.foutin.nio.netty.simple.cray.im.command;

import java.util.Scanner;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 15:04
 */
public interface BaseCommand {

    //获取命令的key
    String getKey();

    //获取命令的提示信息
    String getTip();

    //从控制台提取 业务数据
    void exec(Scanner scanner);
}
