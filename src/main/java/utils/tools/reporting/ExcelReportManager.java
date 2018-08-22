package utils.tools.reporting;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.tools.files.FileManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

public abstract class ExcelReportManager {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HHmm");
    private Calendar currentDate = Calendar.getInstance();
    private File file;

    private XSSFWorkbook workbook = new XSSFWorkbook();
    protected XSSFSheet sheet;

    public abstract String getReportName();

    protected abstract Map<String, Integer> getColumnsMapFromEnum();

    public void createReportFile() {
        this.file = createReportFilePattern(getReportName());
        generateColumns(getColumnsMapFromEnum());
    }

    private void generateColumns(Map<String, Integer> columns) {
        sheet = workbook.createSheet(getReportName());
        Row row = sheet.createRow((short) 0);
        for (Map.Entry<String, Integer> column : columns.entrySet()) {
            row.createCell(column.getValue()).setCellValue(column.getKey());
            sheet.autoSizeColumn(column.getValue());
        }
        writeWorkbookToFile();
    }

    protected void writeWorkbookToFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException ioe) {
            System.out.println("Error happened while trying to logging report to file:\n");
        }
    }

    private File createReportFilePattern(String fileName) {
        String fileNameWithDate = String.format("%s" + fileName + ".xlsx", dateFormat.format(currentDate.getTime()));
        return new File(FileManager.ResultFolder.REPORT_FOLDER.getLocalDir(), fileNameWithDate);
    }

    protected void autoSizeColumns() {
        if (sheet.getPhysicalNumberOfRows() > 0) {
            Row row = sheet.getRow(0);
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell cell;
            int columnIndex;
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                columnIndex = cell.getColumnIndex();
                sheet.autoSizeColumn(columnIndex);
            }
        }
    }

    protected CreationHelper getCreationHelper() {
        return workbook.getCreationHelper();
    }

    protected CellStyle createHyperLinkCellStyle() {
        Font hlFont = workbook.createFont();
        hlFont.setUnderline(Font.U_SINGLE);
        hlFont.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        CellStyle hyperlinkStyle = workbook.createCellStyle();
        hyperlinkStyle.setFont(hlFont);
        return hyperlinkStyle;
    }
}
