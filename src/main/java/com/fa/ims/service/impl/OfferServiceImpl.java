package com.fa.ims.service.impl;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.dto.OfferDto;
import com.fa.ims.dto.OfferRecordDto;
import com.fa.ims.entity.*;
import com.fa.ims.enums.ContractType;
import com.fa.ims.repository.*;
import com.fa.ims.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private DepartRepository departRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobLevelRepository jobLevelRepository;


    @Override
    public Optional<Offer> findById(Long offerId) {
        return offerRepository.findById(offerId);
    }

    @Override
    @Transactional
    public Offer saveOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    @Override
    public HashMap<Long, String> getInterviewsMap() {
        List<Object[]> result = offerRepository.findAllInterviewPassWithoutOffer();
        return result.stream()
                .collect(Collectors.toMap(
                        // Key is interviewId (Long)
                        row -> (Long) row[0],
                        // Value is interviewTitle + candidateName (String)
                        row -> row[1] + " / " + row[2],
                        (key1, key2) -> key1,
                        HashMap::new));
    }

    @Override
    public Offer mapOfferDtoToOffer(OfferDto offerDto) {

        return Offer.builder()
                .contractType(ContractType.valueOf(offerDto.getContractType()))
                .offerContFrom(offerDto.getOfferContFrom())
                .offerContTo(offerDto.getOfferContTo())
                .offerDate(offerDto.getOfferDate())
                .offerNotes(offerDto.getOfferNotes())
                .offerSalary(!Objects.equals(offerDto.getOfferSalary(), "") ? Double.parseDouble(offerDto.getOfferSalary()) : 0)
                .departmentOfferId(departRepository.findById(offerDto.getDepartmentOfferId()).orElseThrow())
                .interviewScheduleId(interviewRepository.findById(offerDto.getInterviewScheduleId()).orElseThrow())
                .positionOfferId(positionRepository.findById(offerDto.getPositionOffer()).orElseThrow())
                .userApprovedId(userRepository.findById(offerDto.getUserApprovedId()).orElseThrow())
                .userRecruiterId(userRepository.findById(offerDto.getUserRecruiterId()).orElseThrow())
                .jobLevelId(jobLevelRepository.findById(offerDto.getJobLevelId()).orElseThrow())
                .build();
    }

    @Override
    public OfferDto mapOfferToOfferDto(Offer offer) {

        return OfferDto.builder()
                .offerId(offer.getOfferId())
                .contractType(offer.getContractType().toString())
                .offerContFrom(offer.getOfferContFrom())
                .offerContTo(offer.getOfferContTo())
                .offerDate(offer.getOfferDate())
                .offerNotes(offer.getOfferNotes())
                .offerSalary(String.valueOf(offer.getOfferSalary()))
                .departmentOfferId(offer.getDepartmentOfferId().getDepartmentId())
                .interviewScheduleId(offer.getInterviewScheduleId().getScheduleId())
                .positionOffer(offer.getPositionOfferId().getPositionId())
                .userApprovedId(offer.getUserApprovedId().getId())
                .userRecruiterId(offer.getUserRecruiterId().getId())
                .jobLevelId(offer.getJobLevelId().getLevelId())
                .status(this.getCandidateStatusesNameByOfferId(offer.getOfferId()))
                .lastModifiedDate(offer.getLastModifiedDate().toLocalDate())
                .createdBy(offer.getCreatedBy())
                .createdDate(offer.getCreatedDate().toLocalDate())
                .lastModifiedBy(offer.getLastModifiedBy())
                .candidate(this.getScheduleTitleAndCandidateByOfferId(offer.getOfferId()))
                .notes(this.getScheduleNoteByOfferId(offer.getOfferId()))
                .interviews(this.getAllInterviewerByOfferId(offer.getOfferId()))
                .build();
    }

    @Override
    @Transactional
    public int updateCandidateStatusIdByOfferId(Long offerId, Long statusId) {
        return offerRepository.updateCandidateStatusIdByOfferId(offerId,statusId);
    }

    @Override
    public Long getCandidateStatusesIdByOfferId(Long offerId) {
        return offerRepository.getCandidateStatusesIdByOfferId(offerId);
    }

    @Override
    public String getScheduleTitleAndCandidateByOfferId(Long offerID) {

        List<Object[]> result =   offerRepository.getScheduleTitleAndCandidateByOfferId(offerID);
        return result.stream()
               .map(row -> (String) row[0] + " - " + (String) row[1])
               .collect(Collectors.joining("\n"));
    }

    @Override
    public String getCandidateStatusesNameByOfferId(Long offerId) {
        return offerRepository.getCandidateStatusesNameByOfferId(offerId);
    }

    @Override
    public Page<OfferRecordDto> getAllOfferRecordDto(String valueSearch, String depart, String status, Integer pageNumber) {
        Sort sortByIdDesc = Sort.by("offerId").descending();
        return offerRepository.getAllOfferRecordDto(valueSearch, depart, status, PageRequest.of(pageNumber - 1, CommonConstants.PAGE_SIZE,sortByIdDesc));
    }

    @Override
    public Page<OfferRecordDto> getAllOfferRecordDtoByManagerName( String valueSearch, String depart, String status, Integer pageNumber, String userName) {
        Sort sortByIdDesc = Sort.by("offerId").descending();
        return offerRepository.getAllOfferRecordDtoByManagerName(valueSearch, depart, status, userName, PageRequest.of(pageNumber - 1, CommonConstants.PAGE_SIZE,sortByIdDesc));
    }

    @Override
    public int countAllOfferApproveByManagerName(String userName) {
        return offerRepository.countAllOfferApproveByManagerName(userName);
    }

    @Override
    public List<Department> getAllDepartment() {
        return departRepository.findAll();
    }

    @Override
    public List<OfferRecordDto> getAllOfferDto(LocalDate dateFrom, LocalDate dateTo) {
        return offerRepository.getAllOfferDto(dateFrom, dateTo);
    }

    @Override
    public List<OfferRecordDto> getAllOfferDtoByManagerName(String userName, LocalDate dateFrom, LocalDate dateTo) {
        return offerRepository.getAllOfferDtoByManagerName(userName, dateFrom, dateTo);
    }

    @Override
    public String getAllInterviewerByOfferId(Long offerId) {
        List<User> users = offerRepository.findAllInterviewerByOfferId(offerId);

        return users.stream()
                .map(User::getUserName)
                .collect(Collectors.joining(", "));
    }

    @Override
    public String getScheduleNoteByOfferId(Long offerId) {
        return offerRepository.findInterviewNotesByOfferId(offerId);
    }
}
