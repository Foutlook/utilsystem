package com.foutin.date;

import com.foutin.utils.AthenaDateUtils;
import com.foutin.utils.BaseTest;
import com.foutin.utils.ImageOperationUtils;

/**
 * @author xingkai.fan
 * @description
 * @date 2020/2/21
 */
public class ImageUtilsTest extends BaseTest {

    public static void main(String[] args) throws Exception {
        String resource = "F:/4619.jpg";
        String target = "F:/image-with-watermark.jpg";
        String warter = "F:/water9.png";
//        ImageOperationUtils.setImageWatermark(resource, target, warter);
        String text = "车架号xxxx" + AthenaDateUtils.getDate();
        ImageOperationUtils.setTextWatermark(resource, target, text, 20);
    }
}
