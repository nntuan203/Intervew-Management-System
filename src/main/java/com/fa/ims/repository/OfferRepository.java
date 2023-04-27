package com.fa.ims.repository;

import com.fa.ims.dto.OfferRecordDto;
import com.fa.ims.entity.Offer;
import com.fa.ims.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query("SELECT i.scheduleId, i.scheduleTitle, c.candidateFullname " +
            "FROM Candidate c " +
            "JOIN InterviewSchedule i ON c.candidateId = i.candidateSchedule.candidateId " +
            "LEFT JOIN Offer o ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "WHERE c.candidateStatus.statusName LIKE 'Passed Interview'")
    List<Object[]> findAllInterviewPassWithoutOffer();

    @Query("SELECT i.userInterviewer " +
            "FROM InterviewSchedule i " +
            "WHERE i.scheduleId = ( " +
            "SELECT i.scheduleId FROM InterviewSchedule i " +
            "JOIN Offer o ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "WHERE o.offerId = :offerId)")
    List<User> findAllInterviewerByOfferId(@Param("offerId") Long offerId);

    @Query("SELECT i.scheduleNotes FROM InterviewSchedule i " +
            "JOIN Offer o ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "WHERE o.offerId = :offerId ")
    String findInterviewNotesByOfferId(@Param("offerId") Long offerId);

    @Query("SELECT i.scheduleTitle, c.candidateFullname " +
            "FROM Candidate c " +
            "JOIN InterviewSchedule i ON c.candidateId = i.candidateSchedule.candidateId " +
            "JOIN Offer o ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "WHERE o.offerId = :offerId ")
    List<Object[]> getScheduleTitleAndCandidateByOfferId(@Param("offerId") Long offerId);

    @Modifying
    @Transactional
    @Query("UPDATE Candidate c " +
            "SET c.candidateStatus.statusId = :statusId " +
            "WHERE c.candidateId IN " +
            "(SELECT i.candidateSchedule.candidateId FROM InterviewSchedule i " +
            "JOIN Offer o ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "WHERE o.offerId = :offerId)")
    int updateCandidateStatusIdByOfferId(@Param("offerId") Long offerId, @Param("statusId") Long statusId);

    @Query("SELECT c.candidateStatus.statusId " +
            "FROM Candidate c " +
            "JOIN InterviewSchedule i ON c.candidateId = i.candidateSchedule.candidateId " +
            "JOIN Offer o ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "WHERE o.offerId = :offerId")
    Long getCandidateStatusesIdByOfferId(@Param("offerId") Long offerId);

    @Query("SELECT c.candidateStatus.statusName " +
            "FROM Candidate c " +
            "JOIN InterviewSchedule i ON c.candidateId = i.candidateSchedule.candidateId " +
            "JOIN Offer o ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "WHERE o.offerId = :offerId")
    String getCandidateStatusesNameByOfferId(@Param("offerId") Long offerId);

    @Query("SELECT new com.fa.ims.dto.OfferRecordDto" +
            "(o.offerId, c.candidateFullname, c.candidateEmail, o.userApprovedId.userFullname," +
            " o.departmentOfferId.departmentName, o.offerNotes, c.candidateStatus.statusName) " +
            "FROM Offer o " +
            "JOIN InterviewSchedule i ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "JOIN Candidate c ON c.candidateId = i.candidateSchedule.candidateId " +
            "JOIN CandidateStatus s ON c.candidateStatus.statusId = s.statusId " +
            "WHERE (:depart IS NULL OR o.departmentOfferId.departmentName = :depart) " +
            "AND (:status IS NULL OR c.candidateStatus.statusName = :status ) AND " +
            "(c.candidateFullname LIKE %:value% OR c.candidateEmail LIKE %:value% OR " +
            "o.userApprovedId.userFullname LIKE %:value% OR o.offerNotes LIKE %:value%)")
    Page<OfferRecordDto> getAllOfferRecordDto(@Param("value") String value, @Param("depart") String depart, @Param("status") String status, Pageable pageable);

    @Query("SELECT new com.fa.ims.dto.OfferRecordDto" +
            "(o.offerId, c.candidateFullname, c.candidateEmail, o.userApprovedId.userFullname," +
            " o.departmentOfferId.departmentName, o.offerNotes, c.candidateStatus.statusName) " +
            "FROM Offer o " +
            "JOIN InterviewSchedule i ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "JOIN Candidate c ON c.candidateId = i.candidateSchedule.candidateId " +
            "JOIN CandidateStatus s ON c.candidateStatus.statusId = s.statusId " +
            "WHERE (:depart IS NULL OR o.departmentOfferId.departmentName = :depart) " +
            "AND (:status IS NULL OR c.candidateStatus.statusName = :status ) AND " +
            "(c.candidateFullname LIKE %:value% OR c.candidateEmail LIKE %:value% OR " +
            "o.userApprovedId.userFullname LIKE %:value% OR o.offerNotes LIKE %:value%) AND o.userApprovedId.userName = :userName")
    Page<OfferRecordDto> getAllOfferRecordDtoByManagerName(@Param("value") String value, @Param("depart") String depart, @Param("status") String status,
                                                       @Param("userName") String userName, Pageable pageable);

    @Query("SELECT COUNT(o) " +
            "FROM Offer o " +
            "JOIN InterviewSchedule i ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "JOIN Candidate c ON c.candidateId = i.candidateSchedule.candidateId " +
            "JOIN CandidateStatus s ON c.candidateStatus.statusId = s.statusId " +
            "WHERE c.candidateStatus.statusName LIKE 'Waiting for approval' " +
            "AND o.userApprovedId.userName = :userName")
    int countAllOfferApproveByManagerName(@Param("userName") String userName);

    @Query("SELECT new com.fa.ims.dto.OfferRecordDto" +
            "(o.offerId, c.candidateFullname, c.candidateEmail, o.userApprovedId.userFullname," +
            " o.departmentOfferId.departmentName, o.offerNotes, c.candidateStatus.statusName) " +
            "FROM Offer o " +
            "JOIN InterviewSchedule i ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "JOIN Candidate c ON c.candidateId = i.candidateSchedule.candidateId " +
            "JOIN CandidateStatus s ON c.candidateStatus.statusId = s.statusId " +
            "AND o.offerDate BETWEEN :dateFrom AND :dateTo")
    List<OfferRecordDto> getAllOfferDto( @Param("dateFrom") LocalDate dateFrom,
                                         @Param("dateTo") LocalDate dateTo);

    @Query("SELECT new com.fa.ims.dto.OfferRecordDto" +
            "(o.offerId, c.candidateFullname, c.candidateEmail, o.userApprovedId.userFullname," +
            " o.departmentOfferId.departmentName, o.offerNotes, c.candidateStatus.statusName) " +
            "FROM Offer o " +
            "JOIN InterviewSchedule i ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "JOIN Candidate c ON c.candidateId = i.candidateSchedule.candidateId " +
            "JOIN CandidateStatus s ON c.candidateStatus.statusId = s.statusId " +
            "WHERE o.userApprovedId.userName = :userName " +
            "AND o.offerDate BETWEEN :dateFrom AND :dateTo")
    List<OfferRecordDto> getAllOfferDtoByManagerName(@Param("userName") String userName,
                                                     @Param("dateFrom") LocalDate dateFrom,
                                                     @Param("dateTo") LocalDate dateTo);

    @Query("SELECT new com.fa.ims.dto.OfferRecordDto" +
            "(o.offerId, c.candidateFullname, c.candidateEmail, o.userApprovedId.userFullname," +
            " o.departmentOfferId.departmentName, o.offerNotes, c.candidateStatus.statusName) " +
            "FROM Offer o " +
            "JOIN InterviewSchedule i ON i.scheduleId = o.interviewScheduleId.scheduleId " +
            "JOIN Candidate c ON c.candidateId = i.candidateSchedule.candidateId " +
            "JOIN CandidateStatus s ON c.candidateStatus.statusId = s.statusId " +
            "WHERE c.candidateStatus.statusName LIKE 'Waiting for approval' ")
    List<OfferRecordDto> getAllOfferApprove();
}
