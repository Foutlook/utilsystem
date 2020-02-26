package com.foutin.date;

import com.foutin.utils.AthenaDateUtils;
import com.foutin.utils.BaseTest;
import com.foutin.utils.ImageOperationUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author xingkai.fan
 * @description
 * @date 2020/2/21
 */
public class ImageUtilsTest extends BaseTest {

    public static void main(String[] args) throws Exception {
//        String resource = "F:/IMG_20190406_180608.jpg";
        String resource = "F:/IMG_20190803_170711.jpg";
        String target = "F:/out-picture/image-with-watermark4.jpg";
        String warter = "F:/water9.png";
//        ImageOperationUtils.setImageWatermark(resource, target, warter);
        String text = "车架号xxxx";
            ImageOperationUtils.setTextWatermark(resource, target, text);
//        OutputStream outputStream = new FileOutputStream("F:/out-picture/image-with-watermark1.jpg");
//        ImageOperationUtils.setTextWatermark(resource, outputStream, text);
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        BufferedImage bufferedImage = ImageOperationUtils.setTextWatermark(resource, text);
//        ImageIO.write(bufferedImage, "jpg", os);
//        InputStream is = new ByteArrayInputStream(os.toByteArray());

    }
}
