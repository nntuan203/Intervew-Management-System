package com.fa.ims.service;

import org.springframework.scheduling.annotation.Async;

public interface MailService {

    @Async
    void sendEmailPassword(String username, String password, String email, String title);
    @Async
    void sendMailReminderOffer(String email,String candidateName, String position,
                        String dueDate, String contractUrl, String recruiterName);


    @Async
    void sendMailReminderSchedule(String email, String candidateName, String candidatePosition, String date, String time, String location);
}
