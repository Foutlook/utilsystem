package com.foutin.date;

import com.foutin.utils.BaseTest;
import com.foutin.utils.ImageOperationUtils;

/**
 * @author xingkai.fan
 * @description
 * @date 2020/2/21
 */
public class ImageUtilsTest extends BaseTest {

    public static void main(String[] args) throws Exception {
        String resource = "F:/test4.png";
        String target = "F:/image-with-watermark.jpg";
        String warter = "F:/water9.png";
        ImageOperationUtils.setImageWatermark(resource, target, warter, null);

    }
}
