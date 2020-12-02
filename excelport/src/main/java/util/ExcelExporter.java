package util;

import annotation.ExcelField;
import javafx.beans.binding.ObjectExpression;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

import static java.util.Comparator.comparing;


/**
 * @author:tyy
 * @date:2020/12/1
 */
public class ExcelExporter {
    /***************************** 成员变量 ****************************/
    private static Logger log = LoggerFactory.getLogger(ExcelExporter.class);

    /**
     * 工作薄对象
     */
    private SXSSFWorkbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;

    /**
     * 当前行号
     */
    private int rowNum;

    /**
     * 注解列表（Object[]{ ExcelField, Field/Method }）
     */
    List<Object[]> annotationList = new ArrayList<>();


    /***************************** 初始化 Excel 表对象 ****************************/
    /**
     * 构造函数
     * @param title 表格标题，传“空值”，表示无标题
     * @param cls   实体对象，通过annotation.ExportField获取标题
     */
    public ExcelExporter(String title, Class<?> cls) {
        // 获取注解list
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs) {
            ExcelField ef = f.getAnnotation(ExcelField.class);
            if (ef != null) {
                annotationList.add(new Object[]{ef, f});
            }
        }
        annotationList.sort(comparing(o -> ((ExcelField) o[0]).sort()));
        // 通过注解获取表头
        List<String> headerList = new ArrayList<>();
        for (Object[] os : annotationList) {
            String t = ((ExcelField) os[0]).title();
            headerList.add(t);
        }
        // 初始化excel表：创建excel表、添加标题、创建表头
        initialize(title, headerList);
    }

    /**
     * 初始化函数
     * @param title      表格标题，传“空值”，表示无标题
     * @param headerList 表头列表
     */
    private void initialize(String title, List<String> headerList) {
        // 创建 excel表
        this.wb = new SXSSFWorkbook();
        this.sheet = wb.createSheet("Export");
        this.styles = createStyles(wb);

        // 创建标题
        if (StringUtils.isNotBlank(title)) {
            Row titleRow = sheet.createRow(rowNum++);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("title"));
            titleCell.setCellValue(title);
            sheet.addMergedRegion(
                    new CellRangeAddress(
                            titleRow.getRowNum(),
                            titleRow.getRowNum(),
                            titleRow.getRowNum(),
                            headerList.size() - 1
                    )
            );
        }

        // 创建表头
        if (headerList == null) {
            throw new RuntimeException("headerList not null!");
        }
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(16);
        for (int i = 0; i < headerList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(styles.get("header"));
            String[] ss = StringUtils.split(headerList.get(i), "**", 2);
            if (ss.length == 2) {
                cell.setCellValue(ss[0]);
                Comment comment = this.sheet.createDrawingPatriarch().createCellComment(
                        new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6)
                );
                comment.setString(new XSSFRichTextString(ss[1]));
                cell.setCellComment(comment);
            } else {
                cell.setCellValue(headerList.get(i));
            }
            sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < headerList.size(); i++) {
            int colWidth = sheet.getColumnWidth(i) * 2;
            sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
        }
        log.debug("Initialize success.");
    }

    /**
     * 创建表格样式
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();

        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(titleFont);
        styles.put("title", style);

        style = wb.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_LEFT);
        styles.put("data1", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        styles.put("data2", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        styles.put("data3", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
//		style.setWrapText(true);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        return styles;
    }

    /***************************** 写入数据到 Excel 表对象 ****************************/
    /**
     * 写入数据
     * @return list 数据列表
     */
    public <E> ExcelExporter setDataList(List<E> dataList) {
        for (E dataObj : dataList) {
            // 添加行
            Row row = this.addRow();

            // 获取数据，并写入单元格
            int cellNo = 0;
            for (Object[] os : annotationList) {
                // 获取成员变量的值
                Object value = null;
                try {
                    value = Reflections.invokeGetter(dataObj, ((Field) os[1]).getName());
                } catch (Exception ex) {
                    log.info(ex.toString());
                    value = "";
                }
                if (value == null) {
                    value = "";
                }

                // 写入单元格
                ExcelField ef = (ExcelField) os[0];
                this.addCell(row, cellNo++, value, ef.align());
            }
        }
        return this;
    }

    /**
     * 添加一行
     * @return 行对象
     */
    private Row addRow() {
        return sheet.createRow(rowNum++);
    }

    /**
     * 添加一个单元格
     * @param row    添加的行
     * @param column 添加列号
     * @param val    添加值
     * @param align  对齐方式（1：靠左；2：居中；3：靠右）
     * @return 单元格对象
     */
    private Cell addCell(Row row, int column, Object val, int align) {
        Cell cell = row.createCell(column);
        String cellFormatString = "@";
        try {
            // 单元格内容
            if (val == null) {
                cell.setCellValue("");
            } else {
                if (val instanceof String) {
                    cell.setCellValue((String) val);
                } else if (val instanceof Integer) {
                    cell.setCellValue((Integer) val);
                    cellFormatString = "0";
                } else if (val instanceof Long) {
                    cell.setCellValue((Long) val);
                    cellFormatString = "0";
                } else if (val instanceof Double) {
                    cell.setCellValue((Double) val);
                    cellFormatString = "0.00";
                } else if (val instanceof Float) {
                    cell.setCellValue((Float) val);
                    cellFormatString = "0.00";
                } else if (val instanceof Date) {
                    cell.setCellValue((Date) val);
                    cellFormatString = "yyyy-MM-dd HH:mm";
                } else {
                    cell.setCellValue((String) Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
                            "fieldtype." + val.getClass().getSimpleName() + "Type")).getMethod("setValue", Object.class).invoke(null, val));
                }
            }
            // 单元格样式
            CellStyle style = styles.get("data_column_" + column);
            if (style == null) {
                style = wb.createCellStyle();
                style.cloneStyleFrom(styles.get("data" + (align >= 1 && align <= 3 ? align : "")));
                style.setDataFormat(wb.createDataFormat().getFormat(cellFormatString));
                styles.put("data_column_" + column, style);
            }
            cell.setCellStyle(style);
        } catch (Exception ex) {
            log.info("Set cell value [" + row.getRowNum() + "," + column + "] error: " + ex.toString());
            cell.setCellValue("");
        }
        return cell;
    }


    /***************************** 输出相关 ****************************/
    /**
     * 输出数据流
     * @param os 输出数据流
     */
    public ExcelExporter write(OutputStream os) throws IOException {
        wb.write(os);
        return this;
    }

    /**
     * 输出到文件
     * @param fileName 输出文件名
     */
    public ExcelExporter writeFile(String fileName) throws IOException {
        FileOutputStream os = new FileOutputStream(fileName);
        this.write(os);
        return this;
    }

    /**
     * 输出到http客户端
     * @param fileName 输出文件名
     */
    public ExcelExporter write(HttpServletResponse response, String fileName) throws IOException {
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(fileName));
        write(response.getOutputStream());
        return this;
    }

    /**
     * 清理临时文件
     */
    public ExcelExporter dispose() {
        wb.dispose();
        return this;
    }

}