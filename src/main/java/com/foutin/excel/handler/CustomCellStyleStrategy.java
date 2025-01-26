package com.foutin.excel.handler;

import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractVerticalCellStyleStrategy;
import com.foutin.excel.model.ComplexHeadStyles;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.util.List;
import java.util.Objects;

/**
 * @description: easyExcel 3.x版本，对头加样式的处理方式
 * @date 2021/12/22
 **/
public class CustomCellStyleStrategy extends AbstractVerticalCellStyleStrategy {

    /**
     * 复杂表头自定义样式列表
     */
    private final List<ComplexHeadStyles> headStyles;

    /**
     * 构造方法，创建对象时传入需要定制的表头信息队列
     */
    public CustomCellStyleStrategy(List<ComplexHeadStyles> headStyles) {
        this.headStyles = headStyles;
    }

    @Override
    protected WriteCellStyle headCellStyle(CellWriteHandlerContext context) {
        WriteCellStyle writeCellStyle = new WriteCellStyle();

        for (ComplexHeadStyles complexHeadStyle : headStyles) {
            // 取出队列中的自定义表头信息，与当前坐标比较，判断是否相符
            if (Objects.equals(context.getColumnIndex(), complexHeadStyle.getY())
                    && Objects.equals(context.getRowIndex(), complexHeadStyle.getX())) {
                // 设置自定义的表头样式
                writeCellStyle.setFillForegroundColor(complexHeadStyle.getIndexColor());
            }
        }
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setBold(Boolean.FALSE);
        headWriteFont.setFontHeightInPoints((short) 14);
        writeCellStyle.setWriteFont(headWriteFont);
        // 水平对齐类型
        writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 垂直对齐类型
        writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        writeCellStyle.setShrinkToFit(true);
        writeCellStyle.setLocked(true);

        return writeCellStyle;
    }


}
