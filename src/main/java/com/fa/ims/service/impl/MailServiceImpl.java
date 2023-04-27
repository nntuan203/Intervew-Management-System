package com.fa.ims.service.impl;

import com.fa.ims.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmailPassword(String username, String password, String email, String title) {
        SimpleMailMessage mail = new SimpleMailMessage();
        String mailBody = "Your Username: " + username + "\n" + " Your Password: "  + password;
        String date = (new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime()));
        mail.setTo(email);
        mail.setFrom("dung1709cht@gmail.com");
        mail.setSubject("No-reply-email-IMS-system <Account " + title + ">");
        mail.setText("This email is from IMS system,\n" +
                "\nYour account has been " + title +". Please use the following credential to login:\n" + mailBody);
        mailSender.send(mail);
    }
    @Override
    public void sendMailReminderOffer(String email,String candidateName, String position,
                               String dueDate, String contractUrl, String recruiterName) {

        String subject = "no-reply-email-IMS-system <Take action on Job offer>";
        String body = "This email is from IMS system\n\n" +
                "You have an offer to take action For Candidate %s " +
                "position %s before %s, the contract " +
                "is attached with this no-reply-email\n\n" +
                "Please refer this link to take action %s\n\n" +
                "If anything wrong, please reach-out recruiter %s. We are so sorry for this inconvenience.\n\n" +
                "Thanks & Regards!\n" +
                "IMS Team.";

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom("dung1709cht@gmail.com");
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(String.format(body, candidateName, position, dueDate, contractUrl, recruiterName));

        mailSender.send(mail);
    }

    @Override
    public void sendMailReminderSchedule(String email, String candidateName, String candidatePosition, String date, String time, String location) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String subject = "no-reply-email-IMS-System <Reminder upcoming interview schedule>";
        String body = "Dear %s,\n" +
                "I am writing to remind you of our scheduled interview for the position of %s " +
                "on %s/%s at %s.  I am looking forward to meeting with you and " +
                "discussing how my qualifications and experience align with this opportunity.\n" +
                "However, I wanted to reiterate that the schedule has been set, and I am hoping for your presence." +
                "If you have any reason to reschedule, please let me know immediately so we can discuss a new interview date.\n" +
                "I am excited about the opportunity to meet with you and appreciate your consideration of my application.\n" +
                "Sincerely,\n" +
                "IMS Team.\n";

        mailMessage.setFrom("dung1709cht@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(String.format(body, candidateName, candidatePosition, date, time, location));

        mailSender.send(mailMessage);
    }
}
