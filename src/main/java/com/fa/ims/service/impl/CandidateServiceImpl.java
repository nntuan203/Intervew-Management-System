package com.fa.ims.service.impl;

import com.fa.ims.dto.CandidateDetailDto;
import com.fa.ims.dto.CandidateDto;
import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.CandidateStatus;
import com.fa.ims.entity.Position;
import com.fa.ims.entity.Skill;
import com.fa.ims.repository.*;
import com.fa.ims.service.CandidateService;
import com.fa.ims.service.FileService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fa.ims.constant.CommonConstants.Candidate_Status_Create;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private HighestLevelRepository highestLevelRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SkillsRepository skillsRepository;

    @Autowired
    private UserListRepository userListRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private CandidateStatusRepository candidateStatusRepository;


    @Override
    public CandidateDto mapperCandidateDtoFromCandidate(Candidate candidate) {
        TypeMap<Candidate, CandidateDto> typeMap = modelMapper.getTypeMap(Candidate.class, CandidateDto.class);
        if(typeMap == null) {
            typeMap = this.modelMapper.createTypeMap(Candidate.class, CandidateDto.class);
            typeMap.addMappings(modelMapper -> modelMapper.skip(CandidateDto :: setCandidateStatus))
                    .addMappings(modelMapper -> modelMapper.skip(CandidateDto :: setPositionCandidate))
                    .addMappings(modelMapper -> modelMapper.skip(CandidateDto :: setHighestLevel))
                    .addMappings(modelMapper -> modelMapper.skip(CandidateDto :: setUserRecruiter))
                    .addMappings(modelMapper -> modelMapper.skip(CandidateDto :: setCandidateCV));
        }
        CandidateDto candidateDto = modelMapper.map(candidate, CandidateDto.class);
        candidateDto.setCandidateStatus(candidate.getCandidateStatus().getStatusName());
        candidateDto.setPositionCandidate(candidate.getPositionCandidate().getPositionId());
        candidateDto.setHighestLevel(candidate.getHighestLevel().getHighestId());
        candidateDto.setUserRecruiter(candidate.getUserRecruiter().getId());
        candidateDto.setUriPath(candidate.getCandidateCv());
        String fileName = candidate.getCandidateCv().split("\\\\")[1];
        candidateDto.setFileName(fileName);
        candidateDto.setCandidateSkills(candidate.getSkills()
                .stream()
                .map(n -> n.getSkillsId()).collect(Collectors.toList()));
        return candidateDto;
    }


    @Override
    public Candidate mapperCandidateFromCandidateDto(CandidateDto candidateDto) {
        Candidate candidate = modelMapper.map(candidateDto, Candidate.class);
        candidate.setCandidateFullname(candidate.getCandidateFullname().trim());

        List<Skill> skills = new ArrayList<>();
        for (Long s: candidateDto.getCandidateSkills()) {
            skills.add(skillsRepository.findSkillBySkillsId(s));
        }
        candidate.setHighestLevel(highestLevelRepository.findByHighestId(candidateDto.getHighestLevel()));
        candidate.setSkills(skills);
        candidate.setUserRecruiter(userListRepository.findUserById(candidateDto.getUserRecruiter()));
        candidate.setPositionCandidate(positionRepository.findPositionByPositionId(candidateDto.getPositionCandidate()));
        candidate.setCandidateStatus(candidateStatusRepository.findCandidateStatusByStatusName(candidateDto.getCandidateStatus()));
        return candidate;
    }

    @Transactional
    @Override
    public Boolean saveNewCandidate(CandidateDto candidateDto) {
        Candidate candidate = this.mapperCandidateFromCandidateDto(candidateDto);
        String userFolder = fileService.saveFile(candidateDto.getCandidateCV(), candidateDto.getCandidateEmail());
        candidate.setCandidateCv(userFolder);
        return candidateRepository.save(candidate) != null;
    }

    @Override
    public CandidateDto findCandidateDtoById(Long id) {
        Candidate candidate =  candidateRepository.findById(id).orElseThrow();
        return mapperCandidateDtoFromCandidate(candidate);
    }

    public Candidate findCandidateById(Long id) {
        return candidateRepository.findById(id).orElseThrow();
    }

    @Override
    public Boolean isEmailExists(String email) {

        return candidateRepository.isEmailExists(email) > 0;
    }

    @Override
    public Boolean isSkillMoreThan6(CandidateDto candidateDto) {
        return candidateDto.getCandidateSkills().size() > 6;
    }

    @Override
    public Boolean isReadOnly(String status) {
        return !Candidate_Status_Create.get(0).equals(status);
    }

    @Override
    public Boolean isEmailExist(String email, Long id) {
        if(candidateRepository.findById(id).orElseThrow().getCandidateEmail().equals(email)) {
            return false;
        }
        return this.isEmailExists(email);
    }

    @Transactional
    @Override
    public Boolean delete(Long id) {
        Candidate candidate = candidateRepository.findByCandidateIdAndDeletedFalse(id);
        candidate.setDeleted(true);
        candidateRepository.save(candidate);
        return true;
    }

    @Override
    public Page<Candidate> findCandidateByKeyWord(int pageNo, int pageSize, String keyword, String status) {
        Pageable pageable = PageRequest.of(pageNo -1 , pageSize);
        if(keyword.equals("") && status.equals("")) {
            return findPaginated(pageNo, pageSize);
        }
        return this.candidateRepository.search(keyword, status, pageable);
    }

    @Override
    public List<CandidateStatus> findAllStatus() {
        return candidateStatusRepository.findAll();
    }

    @Override
    public CandidateDetailDto findCandidateDetail(Long id) {
        Candidate candidate = candidateRepository.findByCandidateIdAndDeletedFalse(id);
        CandidateDetailDto candidateDetailDto = CandidateDetailDto.builder()
                .candidateFullname(candidate.getCandidateFullname())
                .candidateEmail(candidate.getCandidateEmail())
                .candidateBirth(candidate.getCandidateBirth().toString())
                .candidateAddress(candidate.getCandidateAddress())
                .candidatePhone(candidate.getCandidatePhone())
                .candidateGender(candidate.getCandidateGender().name())
                .uriPath(candidate.getCandidateCv())
                .fileName(candidate.getCandidateCv().split("\\\\")[1])
                .candidateStatus(candidate.getCandidateStatus().getStatusName())
                .positionCandidate(candidate.getPositionCandidate().getPositionDesc())
                .candidateYoe(candidate.getCandidateYoe())
                .candidateSkills(candidate.getSkills()
                        .stream().map(n -> n.getSkillsDesc())
                        .collect(Collectors.joining(", ")))
                .highestLevel(candidate.getHighestLevel().getHighestDesc())
                .userRecruiter(candidate.getUserRecruiter().getUserName())
                .candidateNote(candidate.getCandidateNote()).build();
        return candidateDetailDto;
    }

    @Transactional
    @Override
    public Candidate saveUpdateCandidate(CandidateDto candidateDto, Long id) {
        Candidate candidate = this.mapperCandidateFromCandidateDto(candidateDto);
        candidate.setCandidateId(id);
        candidate.setCandidateCv(findCandidateById(id).getCandidateCv());
        return candidateRepository.save(candidate);
    }

    @Override
    public Page<Candidate> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.candidateRepository.findAllByDeletedIsFalse(pageable);
    }

}
