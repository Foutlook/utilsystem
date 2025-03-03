package com.foutin.juc.od;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author f2485
 * @Description
 * @date 2025/2/27 17:09
 */
public class OD149 {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //读取
        String str = sc.nextLine();
        System.out.println(getMinSum(str));
    }

    //读取字符串中的最小整数和 数字没有限制长度，要用大数处理
    public static BigInteger getMinSum(String str) {
        //字符串str，只包含 a-z A-Z ±
        char[] chars = str.toCharArray();
        int n = chars.length;
        //初始化数字
        BigInteger number = new BigInteger("0");
        //初始化符号
        char sign = '+';

        //存放操作数，正数单个加，负数整体加
        Stack<BigInteger> stack = new Stack<>();

        for (int i = 0; i < n; i++) {
            //因为要找最小和，遇到正号直接加，遇到负号把后面数字拼起来加
            char c = chars[i];

            //遇到数字，判断正数直接入栈、负数整体入栈
            if (Character.isDigit(c)) {
                if (sign == '+') {
                    stack.push(new BigInteger(String.valueOf(c)));
                } else {
                    //拼数字
                    number = number.multiply(new BigInteger("10")).add(new BigInteger(String.valueOf(c)));
                }
            }

            //不是数字，则判断是符号 输入控制只有a-z A-Z ±
            if (!Character.isDigit(c) || i == n - 1) {
                //如果符号为-，把前面整体入栈
                if (sign == '-') {
                    //把前面整体入栈
                    stack.push(number.multiply(new BigInteger("-1")));
                }
                //刷新符号 遇到字母的话刷新为+号
                sign = c == '-' ? '-' : '+';
                //刷新数字
                number = new BigInteger("0");
            }
        }
        //返回stack求和
        return stack.stream().reduce(BigInteger::add).orElse(new BigInteger("0"));
    }
}
