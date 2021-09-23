package com.mj.taskagile.domain.common.mail;

public interface MailManager {
    
    void send(String emailAddress, String subject, String template, MessageVariable... variables);
}
