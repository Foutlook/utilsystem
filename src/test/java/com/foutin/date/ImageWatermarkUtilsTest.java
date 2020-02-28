package com.foutin.date;

import com.foutin.utils.ImageWatermarkUtils;
import com.foutin.utils.WatermarkParam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xingkai.fan
 * @description
 * @date 2020/2/26
 */
public class ImageWatermarkUtilsTest {

    public static void main(String[] args) throws IOException {
//        String resource = "F:/IMG_20190406_180608.jpg";
        String resource = "F:/IMG_20190803_170711.jpg";
        String target = "F:/out-picture/jjfjdif.jpg";
        String warter = "F:/water9.png";
//        ImageOperationUtils.setImageWatermark(resource, target, warter);
        String text = "车架号xxxx";
//        OutputStream outputStream = new FileOutputStream("F:/out-picture/image-with-watermark1.jpg");
        /*ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageWatermarkUtils.setDefaultTextWatermarkAndWriteStream("F:/bigPicture.jpg", outputStream, text);
        byte[] bytes = outputStream.toByteArray();
        FileOutputStream fileOutputStream = new FileOutputStream("F:/out-picture/ik5.jpg");
        fileOutputStream.write(bytes);

        outputStream.close();
        fileOutputStream.close();*/

        /*String path = "http://wwwarehouse.oss-cn-hangzhou.aliyuncs.com/model/big/cbf51e804367471696a110295f45af12.jpg";
        URL url = new URL(path);
        URLConnection con = url.openConnection();
        BufferedImage read = ImageIO.read(con.getInputStream());
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ImageIO.write(read, "jpg", byteOutputStream);
        byte[] bytes1 = byteOutputStream.toByteArray();*/

        FileInputStream inputStream = new FileInputStream(/*"F:/46.jpg"*/ "F:/IMG_20190803_170711.jpg");
        BufferedImage read1 = ImageIO.read(inputStream);
        ByteArrayOutputStream byteOutputStream1 = new ByteArrayOutputStream();
        ImageIO.write(read1, "jpg", byteOutputStream1);
        byte[] bytes1 = byteOutputStream1.toByteArray();

        List<String> textList = new ArrayList<>();
        textList.add("车架号xxxx-fanxingkai11111111212125555556");
        textList.add("日期：2020年2月27日");
        /*textList.add("操作人：fanll");
        textList.add("品牌：8989");
        textList.add("车系：j000j");
        textList.add("车型：0182-3");
*/
        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        byte[] bytes2 = ImageWatermarkUtils.setDefaultTextWatermark(bytes1, textList);
//        byte[] bytes2 = ImageWatermarkUtils.setCustomTextWatermarkByFontShadows(bytes1, textList, 0.5f);
        byte[] bytes2 = ImageWatermarkUtils.setCustomTextWatermarkByPosition(bytes1, textList, WatermarkParam.WATERMARK_POSITION.CENTER_LEFT.getPosition());
//        byte[] bytes2 = ImageWatermarkUtils.setCustomTextWatermarkByFont(bytes1, textList, "fangzhengheiti", 20, Color.white);

        FileOutputStream fileOutputStream = new FileOutputStream("F:/out-picture/ddd大1.jpg");
        fileOutputStream.write(bytes2);
        fileOutputStream.close();
        os.close();
    }

}
