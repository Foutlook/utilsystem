package com.foutin.zk.lock;

import com.foutin.utils.BaseTest;
import com.foutin.zookeeper.lock.service.ZKDemoLockService;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/9/4 17:48
 */
public class ZKDemoTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZKDemoTest.class);

    @Autowired
    private ZKDemoLockService zkDemoLockService;

    @Test
    public void zookeeperService() {
//        zkDemoLockService.demoReentrantLock();
        zkDemoLockService.demoReadWriteLock();
    }

    @Test
    public void unCompressed() throws IOException {
        /*String rarPath = "D:\\data\\fcc631e77f6f448390aa275a2822aa45.zip";
        File file = new File(rarPath);
        InputStream fileInputStream = new FileInputStream(file);
        byte[] data = new byte[fileInputStream.available()];
        fileInputStream.read(data);
        fileInputStream.close();
        //获取流
        InputStream inputStream = new ByteArrayInputStream(data);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream, Charset.forName("GBK"));
        InputStreamReader inputStreamReader = new InputStreamReader(zipInputStream, "GBK");
        //不解压直接读取,加上gbk解决乱码问题,ZipInputStream转BufferedReader
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        //把InputStream转成ByteArrayOutputStream 多次使用
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //此类用于表示 ZIP 文件条目
        ZipEntry zipEntry;
        Workbook workbook = null;

        List<String> picNames = new ArrayList<>();

        List<Map<String, Object>> picFileList = new ArrayList<>();

        LOGGER.info("rar:{}",zipInputStream.getNextEntry());
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.isDirectory()) {
                //如果是目录，不处理
                continue;
            }
            String zipFileName = zipEntry.getName();
            if (!zipFileName.contains(".")) {
                throw new RuntimeException("无后缀");
            }
            String suffix = zipFileName.substring(zipFileName.lastIndexOf(".") + 1);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = zipInputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
            InputStream arrayInputStream = new ByteArrayInputStream(
                    byteArrayOutputStream.toByteArray());


        }*/
    }

}
