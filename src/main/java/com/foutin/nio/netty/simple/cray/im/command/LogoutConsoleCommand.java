package com.foutin.nio.netty.simple.cray.im.command;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 15:10
 */
@Slf4j
@Data
public class LogoutConsoleCommand implements BaseCommand{

    public static final String KEY = "10";

    @Override
    public void exec(Scanner scanner) {
        log.info("退出命令执行成功");
    }


    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "退出";
    }
}
