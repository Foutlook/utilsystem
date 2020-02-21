package com.foutin.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
import org.springframework.util.ObjectUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author xingkai.fan
 * @description 图片处理类
 * @date 2020/2/21
 */
public class ImageOperationUtils {

    private static final float DEFAULT_OPACITY = 0.25f;

    public static void setImageWatermark(String resourceImageUrl, String targetImageUrl, String waterMarkUrl, Float opacity) throws Exception {
        if (ObjectUtils.isEmpty(opacity)) {
            opacity = DEFAULT_OPACITY;
        }
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
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(waterMarkFile), opacity)
                // 输出到文件
                .toFile(targetFile);
    }

}
