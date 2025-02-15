package com.foutin.nio.netty.simple.cray.im.command;

import lombok.Data;

import java.util.Scanner;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 11:34
 */
@Data
public class LoginConsoleCommand implements BaseCommand {

    public static final String KEY = "1";

    private String userName;
    private String password;

    @Override
    public void exec(Scanner scanner) {

        System.out.println("请输入用户信息(id@password)  ");
        String[] info = null;
        while (true) {
            String input = scanner.next();
            info = input.split("@");
            if (info.length != 2) {
                System.out.println("请按照格式输入(id@password):");
            } else {
                break;
            }
        }
        userName = info[0];
        password = info[1];
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "登录";
    }

}
