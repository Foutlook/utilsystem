package com.foutin.dtdream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author f2485
 * @Description
 * @date 2023/10/24 18:37
 */
public class ExpressCalculate_2485 {

    public static double calculate(String express, double col) throws ScriptException {

        if (express.contains("col")) {
            express = express.replace("col", String.valueOf(col));
        }

        if (express.contains("min")) {
            express = express.replaceAll("min\\((.*?),(.*?)\\)", "Math.min($1,$2)");
        }


        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");

        Object result = engine.eval(express); // 计算表达式的结果
        return Double.parseDouble(result.toString()); // 将结果转换为double类型并返回
    }


    public static void main(String[] args) throws ScriptException {
        // String express = "1+3";
        // String express = "col+2*(3+4)-8/2";
        String express = "min(col,3)+col*(1+2)-3/2";
        

        double aa = Math.min(2.5, 3) + 2.5 * (1 + 2) - 3/2;

        double calculate = calculate(express, 2.5);
        System.out.println(aa);
    }


    public static int add(int a, int b) {
        return a + b;
    }

    public static int subtract(int a, int b) {
        return a - b;
    }

    public static int multiply(int a, int b) {
        return a * b;
    }

    public static double divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("除数不能为0");
        }
        return (double) a / b;
    }


}
