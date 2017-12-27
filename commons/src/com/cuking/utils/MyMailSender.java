package com.cuking.utils;

import com.gxcards.app.config.GxConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;

/**
 * Created by xuchonggao on 2017/2/20.
 */
@Component
public class MyMailSender {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(MyMailSender.class);

    /**
     * 发送邮件方法
     *
     * @param emailAddr
     * @param mailTitle
     * @param mailContent
     * @param
     * @throws Exception
     */
    @Async
    public void send(final String emailAddr, final String mailTitle, final String mailContent) {
        try {
            logger.info("send Mail To: " + emailAddr + " Mail Title: " + mailTitle + " mailConcept: " + mailContent);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("code@gxcards.com");
            message.setTo(emailAddr);
            message.setSubject(MimeUtility.encodeWord(GxConfig.title + ":" + mailTitle, "UTF-8", "Q"));
            message.setText(mailContent);
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("邮件发送异常:", e);
        }
    }

    /**
     * 发送邮件方法
     *
     * @param emailAddr
     * @param mailTitle
     * @param mailContent
     * @param
     * @throws Exception
     */
    @Async
    public void send(final String[] emailAddr, final String mailTitle, final String mailContent) {
        try {
            logger.info("send Mail To: " + emailAddr + " Mail Title: " + mailTitle + " mailConcept: " + mailContent);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("code@gxcards.com");
            message.setTo(emailAddr);
            message.setSubject(MimeUtility.encodeWord(GxConfig.title + ":" + mailTitle, "UTF-8", "Q"));
            message.setText(mailContent);
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("邮件发送异常:", e);
        }
    }

    /**
     * 发送邮件方法
     *
     * @param emailAddr
     * @param mailTitle
     * @param mailContent
     * @param files 文件名加 文件对象
     * @throws Exception
     */
    @Async
    public void send(
            final String[] emailAddr,
            final String mailTitle, final String mailContent, File[] files) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("code@gxcards.com");
            helper.setTo(emailAddr);
            helper.setSubject(mailTitle);
            helper.setText(mailContent);
            for (File file : files) {
                helper.addAttachment(file.getName(),file);
            }
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.error("邮件发送异常:", e);
        }
    }


}
