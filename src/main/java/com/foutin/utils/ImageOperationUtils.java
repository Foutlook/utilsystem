package com.foutin.utils;

import com.baidu.utils.StringUtils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author xingkai.fan
 * @description 图片处理类
 * @date 2020/2/21
 */
public class ImageOperationUtils {

    private static final float DEFAULT_OPACITY = 0.25f;

    /**
     * 添加图片型水印
     * @param resourceImageUrl 需加水印的图片
     * @param targetImageUrl 保存图片路径
     * @param waterMarkUrl 水印图片
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
     * @param resourceImageUrl 须加水印图片
     * @param targetImageUrl 保存图片路径和名
     * @param text 水印文字
     * @param fontSize 字体大小
     * @throws IOException
     */
    public static void setTextWatermark(String resourceImageUrl, String targetImageUrl, String text, int fontSize) throws IOException {

        // 获取原图文件
        File resourceFile = new File(resourceImageUrl);
        File targetFile = new File(targetImageUrl);
        // ImageIO读取图片
        BufferedImage image = ImageIO.read(resourceFile);

        BufferedImage bufferedImage = handleTextWaterMark(text, image.getWidth(), image.getHeight(), fontSize);

        Thumbnails.of(image)
                // 设置图片大小
                .size(image.getWidth(), image.getHeight())
                // 加水印 参数：1.水印位置 2.水印图片 3.不透明度0.0-1.0
                .watermark(Positions.BOTTOM_RIGHT, bufferedImage, DEFAULT_OPACITY)
                // 输出到文件
                .toFile(targetFile);
    }

    /**
     * 操纵文字型水印
     * @param text 水印文本
     * @param width 需要添加水印的图片宽度
     * @param height 需要添加水印的图片长度
     * @return
     */
    private static BufferedImage handleTextWaterMark(String text, int width, int height, int fontSize) {

        final Font font = new Font("宋体", Font.BOLD, fontSize);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);

        g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.setFont(font);

        if (StringUtils.isNotBlank(text)) {
            g.drawString(text, 5, height - fontSize / 2);
        }

        g.dispose();
        return image;
    }

}
