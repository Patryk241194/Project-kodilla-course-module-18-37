package com.crud.tasks.service;

import com.crud.tasks.domain.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleEmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    private MailCreatorService mailCreatorService;

    public void send(final Mail mail) {
        log.info("Starting email preparation...");
        try {
            MimeMessagePreparator mailMessage = createMimeMessage(mail);
            javaMailSender.send(mailMessage);
            log.info("Email has been sent.");
        } catch (MailException e) {
            log.error("Failed to process email sending: " + e.getMessage(), e);
        }
    }


    private MimeMessagePreparator createMimeMessage(final Mail mail) {
        return mimeMessage -> {
            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setTo(mail.getMailTo());
                if (mail.getToCc() != null) {
                    messageHelper.addCc(mail.getToCc());
                }
                messageHelper.setSubject(mail.getSubject());
                messageHelper.setText(mailCreatorService.buildTrelloCardEmail(mail.getMessage()), true);
            } catch (MessagingException e) {
                throw new MailException("Failed to create MimeMessage", e) {
                };
            }
        };
    }

}
