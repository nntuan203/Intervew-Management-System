package com.fa.ims.service;

import com.fa.ims.dto.CandidateDetailDto;
import com.fa.ims.dto.CandidateDto;
import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.CandidateStatus;
import com.fa.ims.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CandidateService {

    CandidateDto mapperCandidateDtoFromCandidate(Candidate candidate);

    Candidate mapperCandidateFromCandidateDto(CandidateDto candidateDto);

    Boolean saveNewCandidate(CandidateDto candidateDto);

    CandidateDto findCandidateDtoById(Long id);

    Candidate saveUpdateCandidate(CandidateDto candidateDto, Long id);

    Page<Candidate> findPaginated(int pageNo, int pageSize);

    Candidate findCandidateById(Long id);

    Boolean isEmailExists(String email);

    Boolean isSkillMoreThan6(CandidateDto candidateDto);

    Boolean isReadOnly(String status);

    Boolean isEmailExist(String email, Long id);

    Boolean delete(Long id);

    Page<Candidate> findCandidateByKeyWord(int pageNo, int pageSize, String keyword, String status);

    List<CandidateStatus> findAllStatus();

    CandidateDetailDto findCandidateDetail(Long id);

    }
