package com.foutin.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.util.ObjectUtils;
import sun.font.FontDesignMetrics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author xingkai.fan
 * @description 给图片加水印
 * @date 2020/2/26
 */
public class ImageWatermarkUtils {

    /**
     * 默认最小字体大小
     */
    private static final int MIN_FONT_SIZE = 5;
    /**
     * 默认文字缩小比例，根据图片大小进行动态设置字体大小
     */
    private static final int REDUCE_SCALE = 35;

    private static final float FONT_SHADOWS_SCALE = 0.5f;

    /**
     * 给图片添加文字型水印,使用默认参数
     *
     * @param resourceImageBytes 需加水印的图片字节数组
     * @param watermarkTexts     水印文字
     * @return BufferedImage
     * @throws IOException
     */
    public static byte[] setDefaultTextWatermark(byte[] resourceImageBytes, List<String> watermarkTexts) throws IOException {
        return setCustomTextWatermark(resourceImageBytes, watermarkTexts, WatermarkParam.FONT_TYPE.MICROSOFT_YAHEI.getName(), 0, Color.white,
                FONT_SHADOWS_SCALE, WatermarkParam.WATERMARK_POSITION.BOTTOM_LEFT.getPosition());
    }

    /**
     * 给图片添加文字型水印，自定义字体
     *
     * @param resourceImageBytes 需加水印图片字节数组
     * @param watermarkTexts     水印内容
     * @param fontType           字体类型
     * @param fontSize           字体大小
     * @param fontColor          字体颜色
     * @return
     * @throws IOException
     */
    public static byte[] setCustomTextWatermarkByFont(byte[] resourceImageBytes, List<String> watermarkTexts, String fontType,
                                                      int fontSize, Color fontColor) throws IOException {
        return setCustomTextWatermark(resourceImageBytes, watermarkTexts, fontType, fontSize, fontColor,
                FONT_SHADOWS_SCALE, WatermarkParam.WATERMARK_POSITION.BOTTOM_LEFT.getPosition());
    }

    /**
     * 给图片添加文字型水印，自定义位置
     *
     * @param resourceImageBytes 需加水印图片字节数组
     * @param watermarkTexts     水印内容
     * @param watermarkPosition  水印位置
     * @return
     * @throws IOException
     */
    public static byte[] setCustomTextWatermarkByPosition(byte[] resourceImageBytes, List<String> watermarkTexts, String watermarkPosition) throws IOException {
        return setCustomTextWatermark(resourceImageBytes, watermarkTexts, WatermarkParam.FONT_TYPE.MICROSOFT_YAHEI.getName(), 0, Color.white,
                FONT_SHADOWS_SCALE, watermarkPosition);
    }

    /**
     * 给图片添加文字型水印，自定义阴影
     *
     * @param resourceImageBytes 需加水印图片字节数组
     * @param watermarkTexts     水印内容
     * @param fontShadowsScale   阴影大小比例， 值为0-1f之间
     * @return
     * @throws IOException
     */
    public static byte[] setCustomTextWatermarkByFontShadows(byte[] resourceImageBytes, List<String> watermarkTexts, float fontShadowsScale) throws IOException {
        return setCustomTextWatermark(resourceImageBytes, watermarkTexts, WatermarkParam.FONT_TYPE.MICROSOFT_YAHEI.getName(), 0, Color.white,
                fontShadowsScale, WatermarkParam.WATERMARK_POSITION.BOTTOM_LEFT.getPosition());
    }

    /**
     * 自定义添加文本水印，返回图片信息对象
     *
     * @param resourceImageBytes 需加水印的图片字节数组
     * @param watermarkTexts     水印文本
     * @param fontSize           字体大小
     * @param fontType           字体类型
     * @param fontShadowsScale   字体阴影
     * @return BufferedImage
     * @throws IOException
     */
    public static byte[] setCustomTextWatermark(byte[] resourceImageBytes, List<String> watermarkTexts, String fontType,
                                                int fontSize, Color fontColor, float fontShadowsScale,
                                                String position) throws IOException {

        BufferedImage image = getImageInfo(resourceImageBytes);
        BufferedImage bufferedImage = handleTextWaterMark(watermarkTexts, image.getWidth(), image.getHeight(), fontSize,
                fontColor, fontType, fontShadowsScale);
        if (ObjectUtils.isEmpty(bufferedImage)) {
            return null;
        }

        Position textPosition = getTextPosition(position);
        if (ObjectUtils.isEmpty(textPosition)) {
            throw new RuntimeException("输入的水印位置不正确");
        }

        BufferedImage asBufferedImage = getThumbnails(image, bufferedImage, textPosition).asBufferedImage();
        byte[] imageBytes;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(asBufferedImage, "jpg", os);
            imageBytes = os.toByteArray().clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return imageBytes;
    }

    private static Thumbnails.Builder<BufferedImage> getThumbnails(BufferedImage sourceImage, BufferedImage watermarkImage, Position position) {

        return Thumbnails.of(sourceImage)
                // 设置图片大小
                .size(sourceImage.getWidth(), sourceImage.getHeight())
                // 加水印
                .watermark(position, watermarkImage, WatermarkParam.FONT_OPACITY.OPACITY_100.getOpacity())
                .outputFormat("jpg");
    }


    /**
     * 操纵文字型水印
     *
     * @param watermarkTexts 水印文本
     * @param width          需要添加水印的图片宽度
     * @param height         需要添加水印的图片长度
     * @return
     */
    private static BufferedImage handleTextWaterMark(List<String> watermarkTexts, int width, int height, int fontSize,
                                                     Color fontColor, String fontType, float fontShadowsScale) {
        if (ObjectUtils.isEmpty(watermarkTexts)) {
            return null;
        }

        if (fontSize == 0) {
            int size = width > height ? height : width;
            //根据图片大小动态设置字体大小
            fontSize = size / REDUCE_SCALE < MIN_FONT_SIZE ? MIN_FONT_SIZE : size / REDUCE_SCALE;
        }
        // 设置字体阴影大小
        float offset = (float) (fontSize * fontShadowsScale * 0.15);
        final Font font = new Font(fontType, Font.BOLD, fontSize);
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int wordHeight = metrics.getHeight() * (watermarkTexts.size() + 1);
        int wordWidth = 0;
        for (String watermarkText : watermarkTexts) {
            int xLength = getWordWidth(metrics, watermarkText);
            if (xLength > wordWidth) {
                wordWidth = xLength;
            }
        }
        if (wordWidth > width) {
            wordWidth = width;
        }

        BufferedImage image = new BufferedImage(wordWidth + 1, wordHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(wordWidth + 1, wordHeight, Transparency.TRANSLUCENT);

        g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);

        // 设置阴影
        drawStringByGraphics(g, 1, wordHeight, watermarkTexts, Color.black, fontSize, offset);
        // 设置文字
        drawStringByGraphics(g, 1, wordHeight, watermarkTexts, fontColor, fontSize, 0);

        g.dispose();
        return image;
    }

    private static void drawStringByGraphics(Graphics2D g, float x, float y, List<String> watermarkTexts, Color color,
                                             float fontSize, float offset) {
        g.setColor(color);
        float size = (float) (watermarkTexts.size() * 1.2);
        for (String watermarkText : watermarkTexts) {
            float gapSize = fontSize * size;
            if (offset != 0) {
                g.drawString(watermarkText, x + offset, (y - gapSize) + offset);
            } else {
                g.drawString(watermarkText, x, y - gapSize);
            }
            size -= 1.2;
        }
    }

    private static BufferedImage getImageInfo(byte[] resourceImageBytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(resourceImageBytes);
        // ImageIO读取图片
        return ImageIO.read(inputStream);
    }

    private static int getWordWidth(FontDesignMetrics metrics, String content) {
        int width = 0;
        for (int i = 0; i < content.length(); i++) {
            width += metrics.charWidth(content.charAt(i));
        }
        return width;
    }

    private static Position getTextPosition(String position) {

        if (ObjectUtils.isEmpty(position)) {
            return null;
        }
        Position wordPosition = null;
        switch (position) {
            case "TOP_LEFT":
                wordPosition = Positions.TOP_LEFT;
                break;
            case "TOP_CENTER":
                wordPosition = Positions.TOP_CENTER;
                break;
            case "TOP_RIGHT":
                wordPosition = Positions.TOP_RIGHT;
                break;
            case "CENTER_LEFT":
                wordPosition = Positions.CENTER_LEFT;
                break;
            case "CENTER":
                wordPosition = Positions.CENTER;
                break;
            case "CENTER_RIGHT":
                wordPosition = Positions.CENTER_RIGHT;
                break;
            case "BOTTOM_LEFT":
                wordPosition = Positions.BOTTOM_LEFT;
                break;
            case "BOTTOM_CENTER":
                wordPosition = Positions.BOTTOM_CENTER;
                break;
            case "BOTTOM_RIGHT":
                wordPosition = Positions.BOTTOM_RIGHT;
                break;
            default:
        }

        return wordPosition;
    }

}
