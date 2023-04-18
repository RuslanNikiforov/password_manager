package ruslan.password_manager.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ruslan.password_manager.entity.ApplicationPassword;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ApplicationPasswordsExcelExport {

    private List<ApplicationPassword> passwordsList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final static Logger LOG = LoggerFactory.getLogger(ApplicationPasswordsExcelExport.class);

    public ApplicationPasswordsExcelExport(List<ApplicationPassword> passwordsList) {
        this.passwordsList = passwordsList;
        workbook = new XSSFWorkbook();
    }

    public CellStyle getHeadersStyle() {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(18);
        style.setFont(font);
        return style;
    }

    public void writeHeaders() {
        sheet = workbook.createSheet("Application passwords");
        Row headerRow = sheet.createRow(0);
        CellStyle style = getHeadersStyle();
        fillValuesToHeaders(headerRow, style);
    }

    public void fillValuesToHeaders(Row headerRow, CellStyle style) {
        createCell(headerRow, 0, "Название приложения", style);
        createCell(headerRow, 1, "Дата последнего изменения", style);
        createCell(headerRow, 2, "Пароль", style);
    }

    public void createCell(Row row, int columnValue, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnValue);
        Cell createdCell = row.createCell(columnValue);
        createdCell.setCellValue((String) value);
        createdCell.setCellStyle(style);
    }

    public void fillData() {
        CellStyle dataStyle = getDataStyle();
        for (int i = 0; i < passwordsList.size(); i++) {
            ApplicationPassword currentApplicationPassword = passwordsList.get(i);
            Row currentRow = sheet.createRow(i + 1);
            fillValuesToRow(currentRow, dataStyle, currentApplicationPassword);
        }
    }

    public void fillValuesToRow(Row row, CellStyle dataStyle, ApplicationPassword applicationPassword) {
        createCell(row, 0, applicationPassword.getAppName(), dataStyle);
        createCell(row, 1, applicationPassword.getFormattedLastModified(), dataStyle);
        createCell(row, 2, applicationPassword.getPassword(), dataStyle);
    }

    public CellStyle getDataStyle() {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        return style;
    }

    public void export(HttpServletResponse response) {
        writeHeaders();
        fillData();
        try {
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
            out.close();
        }
        catch (IOException e) {
            LOG.debug(e.getMessage(), e.getCause());
        }
    }
}
