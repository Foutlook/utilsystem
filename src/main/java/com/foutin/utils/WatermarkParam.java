package com.foutin.utils;

/**
 * 水印参数
 */
public class WatermarkParam {

    /**
     * 位置
     */
    public enum WATERMARK_POSITION {
        TOP_LEFT("TOP_LEFT"),
        TOP_CENTER("TOP_CENTER"),
        TOP_RIGHT("TOP_RIGHT"),
        CENTER_LEFT("CENTER_LEFT"),
        CENTER("CENTER"),
        CENTER_RIGHT("CENTER_RIGHT"),
        BOTTOM_LEFT("BOTTOM_LEFT"),
        BOTTOM_CENTER("BOTTOM_CENTER"),
        BOTTOM_RIGHT("BOTTOM_RIGHT"),

        ;
        private String position;

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        WATERMARK_POSITION(String position) {
            this.position = position;
        }

        public boolean equalByCode(String position) {
            return this.position != null && this.position.equals(position);
        }

        public static WATERMARK_POSITION getByCode(String position) {
            for (WATERMARK_POSITION item : values()) {
                if (item.position.equals(position)) {
                    return item;
                }
            }
            return null;
        }
    }

    /**
     * 字体
     */
    public enum FONT_TYPE {

        MICROSOFT_YAHEI("微软雅黑", "微软雅黑"),
        WQY_ZENHEI("wqy-zenhei", "文泉驿正黑"),
        WQY_MICROHEI("wqy-microhei", "文泉微米黑"),
        FANGZHENGSHUSONG("fangzhengshusong", "方正书宋"),
        FANGZHENGKAITI("fangzhengkaiti", "方正楷体"),
        FANGZHENGHEITI("fangzhengheiti", "方正黑体"),
        FANGZHENGFANGSONG("fangzhengfangsong", "方正仿宋"),
        DROIDSANSFALLBACK("droidsansfallback", "droidsansfallback");

        private String name;
        private String description;

        FONT_TYPE(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean equalByCode(String name) {
            return this.name != null && this.name.equals(name);
        }

        public static FONT_TYPE getByCode(String name) {
            for (FONT_TYPE item : values()) {
                if (item.name.equals(name)) {
                    return item;
                }
            }
            return null;
        }
    }

    /**
     * 透明度
     */
    public enum FONT_OPACITY {

        OPACITY_100(1f, "不透明"),
        OPACITY_50(0.5f, "半透明"),
        OPACITY_0(0f, "全透明"),

        ;

        private Float opacity;
        private String description;

        FONT_OPACITY(Float opacity, String description) {
            this.opacity = opacity;
            this.description = description;
        }

        public Float getOpacity() {
            return opacity;
        }

        public void setOpacity(Float opacity) {
            this.opacity = opacity;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean equalByCode(Float opacity) {
            return this.opacity != null && this.opacity.equals(opacity);
        }

        public static FONT_OPACITY getByCode(Float opacity) {
            for (FONT_OPACITY item : values()) {
                if (item.opacity.equals(opacity)) {
                    return item;
                }
            }
            return null;
        }
    }


}
