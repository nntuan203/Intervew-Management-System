package com.fa.ims.scheduleTask;

import com.fa.ims.dto.OfferRecordDto;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.repository.InterviewScheduleRepository;
import com.fa.ims.repository.OfferRepository;
import com.fa.ims.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {
    @Autowired
    private InterviewScheduleRepository interviewScheduleRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private OfferRepository offerRepository;


    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

//    second minute hour day-of-month month day-of-week year : cron
//    0 * * ? * * : mỗi phút chạy 1 lần, để test
    @Scheduled(cron = "0 0 0 * * ?",zone = "Asia/Ho_Chi_Minh") // run at 00:00 every day
    public void updateInterviewStatus() {
        List<InterviewSchedule> interviews = interviewScheduleRepository.findAll();
        interviews.forEach(InterviewSchedule::updateStatus);
        interviewScheduleRepository.saveAll(interviews);
        LOGGER.info("Update interview schedule status!");
    }

    @Scheduled(cron = "0 0 8 * * ?", zone = "Asia/Ho_Chi_Minh") // run at 08:00 every day
    public void sendMailReminderOffer() {

        String contractUrl = "http://localhost:8080/offer?valueSearch=&depart=&status=Waiting%20for%20approval";

        List<OfferRecordDto> list = offerRepository.getAllOfferApprove();

        if (!list.isEmpty()) {
            for (OfferRecordDto offer : list) {
                mailService.sendMailReminderOffer(
                        offer.getEmail(),
                        offer.getCandidateName(),
                        offer.getDepartment(),
                        offerRepository.findById(offer.getOfferId()).orElseThrow().getOfferDate().toString(),
                        contractUrl,
                        offer.getApprove());
            }
        }
        LOGGER.info("Reminder to take action on the offer!");
    }

    @Scheduled(cron = "0 0 8 * * ?",zone = "Asia/Ho_Chi_Minh") // run at 08:00 every day
    public void sendMailReminderSchedule() {
        List<InterviewSchedule> interviewScheduleList = interviewScheduleRepository.findAll();
        interviewScheduleList.forEach(interviewSchedule -> {
            if (interviewSchedule.dueDateInterview()) {
                mailService.sendMailReminderSchedule(
                        interviewSchedule.getCandidateSchedule().getCandidateEmail(),
                        interviewSchedule.getCandidateSchedule().getCandidateFullname(),
                        interviewSchedule.getCandidateSchedule().getPositionCandidate().getPositionDesc(),
                        interviewSchedule.getInterviewDate().toString(),
                        interviewSchedule.getInterviewStartTime().toString(),
                        interviewSchedule.getScheduleLocation());
            }
        });
        LOGGER.info("Send email reminder interview schedule!");
    }


}
