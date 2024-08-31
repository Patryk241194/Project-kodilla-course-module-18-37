package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailCreatorService {

    @Autowired
    private AdminConfig adminConfig;
    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;
    @Value("${info.company.name}")
    private String companyName;

    @Value("${info.company.goal}")
    private String companyGoal;

    @Value("${info.company.email}")
    private String companyEmail;

    @Value("${info.company.phone}")
    private String companyPhone;

    public String buildTrelloCardEmail(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("goodbye_message", "Best regards");
        context.setVariable("tasks_url", "https://patryk241194.github.io/");
        context.setVariable("button", "Visit website");
        context.setVariable("admin_name", adminConfig.getAdminName());
        context.setVariable("company_name", companyName);
        context.setVariable("company_goal", companyGoal);
        context.setVariable("company_email", companyEmail);
        context.setVariable("company_phone", companyPhone);
        return templateEngine.process("mail/created-trello-card-mail", context);
    }


}
