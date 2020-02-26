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
        String resource = "F:/IMG_20190406_180608.jpg";
//        String resource = "F:/small_picture.jpg";
        String target = "F:/out-picture/image-with-watermark.jpg";
        String warter = "F:/water9.png";
//        ImageOperationUtils.setImageWatermark(resource, target, warter);
        String text = "车架号xxxx";
//        ImageOperationUtils.setTextWatermark(resource, target, text);
        OutputStream outputStream = new FileOutputStream("F:/out-picture/image-with-watermark.png");
        ImageOperationUtils.setTextWatermark(resource, outputStream, text);
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        BufferedImage bufferedImage = ImageOperationUtils.setTextWatermark(resource, text);
//        ImageIO.write(bufferedImage, "jpg", os);
//        InputStream is = new ByteArrayInputStream(os.toByteArray());

    }
}
