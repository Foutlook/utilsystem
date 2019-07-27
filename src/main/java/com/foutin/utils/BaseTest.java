package com.foutin.utils;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试
 *
 * @author xingkai.fan
 * @create 2019-07-25
 */
@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:spring-config.xml"}) //加载配置文件
public class BaseTest {

}
