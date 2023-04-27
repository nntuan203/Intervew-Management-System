package com.fa.ims.service;

import com.fa.ims.dto.OfferDto;
import com.fa.ims.dto.OfferRecordDto;
import com.fa.ims.entity.Department;
import com.fa.ims.entity.Offer;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OfferService {
    Optional<Offer> findById(Long offerId);

    Offer saveOffer(Offer offer);

    Map<Long, String> getInterviewsMap();

    Offer mapOfferDtoToOffer(OfferDto offerDto);

    OfferDto mapOfferToOfferDto(Offer offer);

    int updateCandidateStatusIdByOfferId(Long offerId, Long statusId);

    Long getCandidateStatusesIdByOfferId(Long offerId);

    String getScheduleTitleAndCandidateByOfferId(Long offerID);

    String getCandidateStatusesNameByOfferId(Long offerId);

    Page<OfferRecordDto> getAllOfferRecordDto(String valueSearch, String depart, String status, Integer pageNumber);

    Page<OfferRecordDto> getAllOfferRecordDtoByManagerName(String valueSearch, String depart, String status, Integer pageNumber, String userName);

    int countAllOfferApproveByManagerName(String userName);

    List<Department> getAllDepartment();

    List<OfferRecordDto> getAllOfferDto(LocalDate dateFrom, LocalDate dateTo);

    List<OfferRecordDto> getAllOfferDtoByManagerName(String userName, LocalDate dateFrom, LocalDate dateTo);

    String getAllInterviewerByOfferId(Long offerId);

    String getScheduleNoteByOfferId(Long offerId);
}
