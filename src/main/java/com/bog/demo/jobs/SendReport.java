package com.bog.demo.jobs;

import com.bog.demo.domain.product.Sell;
import com.bog.demo.repository.sell.SellRepository;
import com.bog.demo.service.product.ProductService;
import com.bog.demo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;

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

    @Scheduled(cron = "0 0 0 1/1 * ?")
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

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("info@pixl.ge");
        message.setTo("tabagari89@gmail.com");
        message.setSubject("Verification");
        message.setText("შესრულებული შესყიდვების რაოდენობა: " + sells.size()
                + "<br>"
                + "შესყიდული პროდუქციის ღირებულების ჯამური თანხა: " + cost
                + "შესყიდვებით მიღებული საკომისიო: " + cost * 0.1);
        emailSender.send(message);
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
