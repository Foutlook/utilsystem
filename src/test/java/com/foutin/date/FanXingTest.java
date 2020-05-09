package com.foutin.date;

import com.foutin.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xingkai.fan
 * @date 2019-12-28
 */
public class FanXingTest {

    public static void main(String[] args) throws ParseException {
        String time = "2019/09/06 0:00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd H:mm");
        Date parse = simpleDateFormat.parse(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String format = dateFormat.format(parse.getTime());
        System.out.println(format);

        Date date = DateUtils.addDay(parse, -3);
        System.out.println(dateFormat.format(date));

        ExecutorService executorService = Executors.newFixedThreadPool(1);
    }
}
