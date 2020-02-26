package com.foutin.utils;

import com.baidu.utils.StringUtils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * @author xingkai.fan
 * @description 图片处理类
 * @date 2020/2/21
 */
public class ImageOperationUtils {

    private static final float DEFAULT_OPACITY = 1f;

    /**
     * 添加图片型水印
     *
     * @param resourceImageUrl 需加水印的图片
     * @param targetImageUrl   保存图片路径
     * @param waterMarkUrl     水印图片
     * @throws Exception
     */
    public static void setImageWatermark(String resourceImageUrl, String targetImageUrl, String waterMarkUrl) throws Exception {

        // 获取原图文件
        File resourceFile = new File(resourceImageUrl);
        File waterMarkFile = new File(waterMarkUrl);
        File targetFile = new File(targetImageUrl);
        // ImageIO读取图片
        BufferedImage image = ImageIO.read(resourceFile);

        Thumbnails.of(image)
                // 设置图片大小
                .size(image.getWidth(), image.getHeight())
                // 加水印 参数：1.水印位置 2.水印图片 3.不透明度0.0-1.0
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(waterMarkFile), DEFAULT_OPACITY)
                // 输出到文件
                .toFile(targetFile);
    }

    /**
     * 给图片添加文字型水印
     *
     * @param resourceImageUrl 须加水印图片
     * @param targetImageUrl   保存图片路径和名
     * @param text             水印文字
     * @throws IOException
     */
    public static void setTextWatermark(String resourceImageUrl, String targetImageUrl, String text) throws IOException {

        // 获取原图文件
        File resourceFile = new File(resourceImageUrl);
        File targetFile = new File(targetImageUrl);
        // ImageIO读取图片
        BufferedImage image = ImageIO.read(resourceFile);

        BufferedImage bufferedImage = handleTextWaterMark(text, image.getWidth(), image.getHeight());

        Thumbnails.of(image)
                // 设置图片大小
                .size(image.getWidth(), image.getHeight())
                // 加水印 参数：1.水印位置 2.水印图片 3.不透明度0.0-1.0
                .watermark(Positions.BOTTOM_RIGHT, bufferedImage, DEFAULT_OPACITY)
                // 输出到文件
                .toFile(targetFile);
    }

    /**
     * 给图片添加文字型水印
     *
     * @param resourceImageUrl 须加水印图片
     * @param text             水印文字
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage setTextWatermark(String resourceImageUrl, String text) throws IOException {

        // 获取原图文件
        File resourceFile = new File(resourceImageUrl);
        // ImageIO读取图片
        BufferedImage image = ImageIO.read(resourceFile);

        BufferedImage bufferedImage = handleTextWaterMark(text, image.getWidth(), image.getHeight());

        return Thumbnails.of(image)
                // 设置图片大小
                .size(image.getWidth(), image.getHeight())
                // 加水印 参数：1.水印位置 2.水印图片 3.不透明度0.0-1.0
                .watermark(Positions.BOTTOM_RIGHT, bufferedImage, DEFAULT_OPACITY)
                // 输出到文件
                .asBufferedImage();
    }

    /**
     * 给图片添加文字型水印
     *
     * @param resourceImageUrl 须加水印图片
     * @param outputStream 输出流
     * @param text             水印文字
     * @return BufferedImage
     * @throws IOException
     */
    public static void setTextWatermark(String resourceImageUrl, OutputStream outputStream, String text) throws IOException {

        // 获取原图文件
        File resourceFile = new File(resourceImageUrl);
        // ImageIO读取图片
        BufferedImage image = ImageIO.read(resourceFile);

        BufferedImage bufferedImage = handleTextWaterMark(text, image.getWidth(), image.getHeight());

        Thumbnails.of(image)
                // 设置图片大小
                .size(image.getWidth(), image.getHeight())
                // 加水印 参数：1.水印位置 2.水印图片 3.不透明度0.0-1.0
                .watermark(Positions.BOTTOM_RIGHT, bufferedImage, DEFAULT_OPACITY)
                .outputFormat("jpg")
                // 输出到文件
                .toOutputStream(outputStream);

    }

    /**
     * 操纵文字型水印
     *
     * @param text   水印文本
     * @param width  需要添加水印的图片宽度
     * @param height 需要添加水印的图片长度
     * @return
     */
    private static BufferedImage handleTextWaterMark(String text, int width, int height) {
        int size = width > height ? height : width;
        //最小字体大小
        int minFontSize = 5;
        //文字缩小比例，根据图片大小进行动态设置字体大小
        int reduceScale = 30;
        //根据图片大小动态设置字体大小
        int fontSize = size / reduceScale < minFontSize ? minFontSize : size / reduceScale;

        final Font font = new Font("微软雅黑", Font.BOLD, fontSize);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);

        g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.setFont(font);

        if (StringUtils.isNotBlank(text)) {
            g.drawString(text, fontSize, height - fontSize * 2);
            g.drawString(AthenaDateUtils.format(new Date(), AthenaDateUtils.FULL_DATE_TIME_FORMAT.getPattern()), fontSize, height - fontSize / 2);
        }

        g.dispose();
        return image;
    }

}
