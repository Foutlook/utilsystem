package com.foutin.date;

import com.foutin.utils.AthenaDateUtils;
import com.foutin.utils.BaseTest;
import com.foutin.utils.ImageOperationUtils;
import org.junit.Test;
import sun.misc.BASE64Encoder;

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
//            ImageOperationUtils.setTextWatermark(resource, target, text);
//        OutputStream outputStream = new FileOutputStream("F:/out-picture/image-with-watermark1.jpg");
        /*ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageOperationUtils.setTextWatermark("F:/bigPicture.jpg", outputStream, text);
        byte[] bytes = outputStream.toByteArray();
        FileOutputStream fileOutputStream = new FileOutputStream("F:/out-picture/image-with-watermark5.jpg");
        fileOutputStream.write(bytes);

        outputStream.close();
        fileOutputStream.close();*/

//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        BufferedImage bufferedImage = ImageOperationUtils.setTextWatermark(resource, text);
//        ImageIO.write(bufferedImage, "jpg", os);
//        InputStream is = new ByteArrayInputStream(os.toByteArray());

        // 阿里云加水印
        testWatermark();

    }

    public static void testWatermark() {
        String path = "http://wwwarehouse.oss-cn-hangzhou.aliyuncs.com/model/big/f3f4d675ed384a6db93a6a0887d7aa46.jpg";
        String defautString = "?x-oss-process=image/watermark";

        String text = "车架号xxxx12121212121213331111111111111111211111111111111111111111111111111111111111";
        String type = "fangzhengshusong";
        String textBase64 = getBase64(text);
        System.out.println(textBase64);
        String typeBase64 = getBase64(type);
        System.out.println(typeBase64);
        String size = "size_30";
        String shadow = "shadow_50";
        String color = "color_FFFFFF";
        String g = "g_east";
        String x = "x_10";
        String y = "y_10";

        String buildPath = path + defautString + "," + "type_" + typeBase64 + "," + size + "," + "text_" + textBase64 + "," + color + "," + shadow + "," + g + "," + x + "," + y;
        System.out.println(buildPath);
    }

    private static String getBase64(String text) {
        BASE64Encoder encodeBase64 = new BASE64Encoder();
        String encode = encodeBase64.encode(text.getBytes());
        String safeBase64Str = encode.replace('+', '-').replace('/', '_');
        System.out.println(safeBase64Str);

        int i = safeBase64Str.lastIndexOf("=");
        int length = safeBase64Str.length();
        System.out.println(i + "----" + length);
        if (safeBase64Str.contains("=")) {
            String str = safeBase64Str;
            safeBase64Str = safeBase64Str.replaceAll("=", "");
            if (str.lastIndexOf("=") == str.length() - 1) {
                safeBase64Str += "=";
            }
        }
        return safeBase64Str;
    }
}
