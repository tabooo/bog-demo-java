package com.bog.demo.jobs;

import com.bog.demo.domain.product.Sell;
import com.bog.demo.model.product.ReportObject;
import com.bog.demo.repository.sell.SellRepository;
import com.bog.demo.service.product.ProductService;
import com.bog.demo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
public class SendReport {
    @Autowired
    private JavaMailSender emailSender;

    private ProductService productService;

    private SellRepository sellRepository;

    @Value("${app.demo.data-directory-path}")
    private String dataDirectory;

    @Scheduled(cron = "0 0 0 1/1 * ?")
//    @Scheduled(fixedDelay = 5000)
    public void scheduleTaskWithCronExpression() {
        Calendar cl = Calendar.getInstance();
        cl.add(Calendar.DATE, -1);
        Date yesterday = cl.getTime();

        Date yesterdayMin = DateUtil.setDayMinTime(yesterday);
        Date yesterdayMax = DateUtil.setDayMaxTime(yesterday);

        List<Sell> sells = sellRepository.getSellsBetweenDates(yesterdayMin, yesterdayMax);

        Double cost = 0.0;
        for (Sell sell : sells) {
            cost += sell.getCost();
        }

        log.info("შესრულებული შესყიდვების რაოდენობა: " + sells.size());

        ReportObject reportObject = new ReportObject();
        reportObject.setSellsCount(sells.size());
        reportObject.setSellsCost(cost);
        reportObject.setSellsSakomisio(cost * 0.1);

        String excelpath = createExcel(reportObject);
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("info@pixl.ge");
            helper.setTo("tabagari89@gmail.com");
            helper.setSubject("Report");

            helper.setText("შესრულებული შესყიდვების რაოდენობა: " + reportObject.getSellsCount()
                    + ", "
                    + "შესყიდული პროდუქციის ღირებულების ჯამური თანხა: " + reportObject.getSellsCost()
                    + ", "
                    + "შესყიდვებით მიღებული საკომისიო: " + reportObject.getSellsSakomisio());

            FileSystemResource file
                    = new FileSystemResource(new File(excelpath));
            helper.addAttachment("report.xlsx", file);

            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createExcel(ReportObject reportObject) {
        try {
            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Report");
            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 4000);

            Row header = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 16);
            font.setBold(true);
            headerStyle.setFont(font);

            // SET HEADERS
            Cell headerCell = header.createCell(0);
            headerCell.setCellValue("დასახელება");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(1);
            headerCell.setCellValue("მნიშვნელობა");
            headerCell.setCellStyle(headerStyle);

            // WRITE CONTENT
            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);

            Row row = sheet.createRow(2);
            Cell cell = row.createCell(0);
            cell.setCellValue("შესრულებული შესყიდვების რაოდენობა:");
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(reportObject.getSellsCount());
            cell.setCellStyle(style);

            row = sheet.createRow(3);
            cell = row.createCell(0);
            cell.setCellValue("შესყიდული პროდუქციის ღირებულების ჯამური თანხა:");
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(reportObject.getSellsCost());
            cell.setCellStyle(style);

            row = sheet.createRow(4);
            cell = row.createCell(0);
            cell.setCellValue("შესყიდვებით მიღებული საკომისიო:");
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(reportObject.getSellsSakomisio());
            cell.setCellStyle(style);

            // SAVE FILE
            File currDir = new File(dataDirectory);
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + "report.xlsx";

            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();

            return fileLocation;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setSellRepository(SellRepository sellRepository) {
        this.sellRepository = sellRepository;
    }
}
